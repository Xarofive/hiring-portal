package ru.kata.project.user.core.usecase;

import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.user.core.dto.AuthenticationResponseDto;
import ru.kata.project.user.core.entity.Role;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserStatus;
import ru.kata.project.user.core.exception.InvalidTokenException;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.TokenRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.service.JwtService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для use-case "Обновление токенов пользователя" {@link RefreshTokenUseCase}
 *
 * @author Vladislav_Bogomolov
 */
@ExtendWith(MockitoExtension.class)
public class RefreshTokenUseCaseTest {
    @Mock
    JwtService jwtService;
    @Mock
    UserRepository userRepository;
    @Mock
    TokenRepository tokenRepository;
    @Mock
    AuthAuditService auditService;

    @InjectMocks
    RefreshTokenUseCase useCase;

    private User activeUser;
    private Token oldToken;
    private String validRefreshToken;

    @BeforeEach
    void setUp() {
        activeUser = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .email("mail@example.com")
                .passwordHash("password_hash")
                .status(UserStatus.ACTIVE)
                .roles(Set.of(new Role(UUID.randomUUID(), "ROLE_CANDIDATE", "Candidate", Set.of())))
                .build();

        oldToken = Token.builder()
                .id(UUID.randomUUID())
                .familyId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .ip("0.0.0.0")
                .fingerprint(null)
                .revokedAt(null)
                .issuedAt(null)
                .expiresAt(null)
                .loggedOut(false)
                .build();

        validRefreshToken = oldToken.getRefreshToken();
    }

    @Test
    void givenValidRefreshToken_whenRefreshTokens_thenRevokeFamilyAndGenerateNewTokens() {
        when(jwtService.extractUsername(validRefreshToken)).thenReturn(activeUser.getUsername());
        when(userRepository.findByUsername(activeUser.getUsername())).thenReturn(Optional.of(activeUser));
        when(tokenRepository.findByRefreshToken(validRefreshToken)).thenReturn(Optional.of(oldToken));
        when(jwtService.isValidRefresh(validRefreshToken, activeUser)).thenReturn(true);
        when(jwtService.generateAccessToken(activeUser)).thenReturn("access_token");
        when(jwtService.generateRefreshToken(activeUser)).thenReturn(validRefreshToken);

        final AuthenticationResponseDto result = useCase.execute(validRefreshToken);

        assertEquals("access_token", result.accessToken());
        assertEquals("refresh_token", result.refreshToken());

        verify(jwtService).revokeToken(oldToken);
        verify(jwtService).saveUserToken(
                eq("access_token"),
                eq(validRefreshToken),
                eq(activeUser),
                any(UUID.class));
        verify(auditService).logAudit(
                eq(activeUser.getId()),
                eq("REFRESH_TOKENS"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenMalformedRefreshToken_whenRefreshTokens_thenThrowsInvalidTokenException() {
        when(jwtService.extractUsername(validRefreshToken)).thenThrow(new UnsupportedJwtException("bad token"));

        final InvalidTokenException ex = assertThrows(InvalidTokenException.class,
                () -> useCase.execute(validRefreshToken));

        assertEquals("Invalid refresh token format", ex.getMessage());
    }

    @Test
    void givenNonExistentRefreshToken_whenRefreshTokens_thenThrowsInvalidTokenException() {
        when(jwtService.extractUsername(validRefreshToken)).thenReturn(activeUser.getUsername());
        when(userRepository.findByUsername(activeUser.getUsername())).thenReturn(Optional.of(activeUser));
        when(tokenRepository.findByRefreshToken(validRefreshToken)).thenReturn(Optional.empty());

        final InvalidTokenException ex = assertThrows(InvalidTokenException.class,
                () -> useCase.execute(validRefreshToken));

        assertEquals("Refresh token not found", ex.getMessage());
    }

    @Test
    void givenNonExistentUser_whenRefreshTokens_thenThrowsUserNotFoundException() {
        when(jwtService.extractUsername(validRefreshToken)).thenReturn("unknown_user");
        when(userRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());

        final UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> useCase.execute(validRefreshToken));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void givenInvalidRefreshTokenForUser_whenRefreshTokens_thenRevokeFamilyAndThrowsInvalidTokenException() {
        when(jwtService.extractUsername(validRefreshToken)).thenReturn(activeUser.getUsername());
        when(userRepository.findByUsername(activeUser.getUsername())).thenReturn(Optional.of(activeUser));
        when(tokenRepository.findByRefreshToken(validRefreshToken)).thenReturn(Optional.of(oldToken));
        when(jwtService.isValidRefresh(validRefreshToken, activeUser)).thenReturn(false);

        final InvalidTokenException ex = assertThrows(InvalidTokenException.class,
                () -> useCase.execute(validRefreshToken));

        assertEquals("Invalid refresh token", ex.getMessage());
        verify(jwtService).revokeFamily(oldToken.getFamilyId());
    }
}
