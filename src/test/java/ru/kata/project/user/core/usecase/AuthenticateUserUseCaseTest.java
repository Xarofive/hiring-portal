package ru.kata.project.user.core.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import ru.kata.project.security.utility.error.AccountDeletedException;
import ru.kata.project.user.core.dto.AuthenticationResponseDto;
import ru.kata.project.user.core.dto.LoginRequestDto;
import ru.kata.project.user.core.entity.Role;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserStatus;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.service.AuthenticationService;
import ru.kata.project.user.core.port.service.JwtService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для use-case "Аутентификация пользователя" {@link AuthenticateUserUseCase}
 *
 * @author Vladislav_Bogomolov
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticateUserUseCaseTest {

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthAuditService auditService;

    @InjectMocks
    private AuthenticateUserUseCase useCase;

    private LoginRequestDto requestWithUsername;
    private LoginRequestDto requestWithEmail;
    private User activeUser;

    @BeforeEach
    void setUp() {
        requestWithUsername = new LoginRequestDto("username", "password");
        requestWithEmail = new LoginRequestDto("mail@example.com", "password");

        activeUser = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .email("mail@example.com")
                .passwordHash("password_hash")
                .status(UserStatus.ACTIVE)
                .roles(Set.of(new Role(UUID.randomUUID(), "ROLE_CANDIDATE", "Candidate", Set.of())))
                .build();
    }

    @Test
    void givenValidCredentials_withUsername_whenAuthenticate_thenTokensGeneratedAndSavedAndAuditLogged() {
        when(userRepository.findByUsernameOrEmail("username"))
                .thenReturn(Optional.of(activeUser));
        when(jwtService.generateAccessToken(activeUser)).thenReturn("access_token");
        when(jwtService.generateRefreshToken(activeUser)).thenReturn("refresh_token");

        final AuthenticationResponseDto result = useCase.execute(requestWithUsername);

        assertEquals("access_token", result.accessToken());
        assertEquals("refresh_token", result.refreshToken());

        verify(authenticationService).authenticate("username", "password");
        verify(jwtService).saveUserToken(
                eq("access_token"),
                eq("refresh_token"),
                eq(activeUser),
                any(UUID.class));
        verify(auditService).logAudit(
                eq(activeUser.getId()),
                eq("AUTHENTICATE"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenValidCredentials_withEmail_whenAuthenticate_thenTokensGeneratedAndSavedAndAuditLogged() {
        when(userRepository.findByUsernameOrEmail("mail@example.com"))
                .thenReturn(Optional.of(activeUser));
        when(jwtService.generateAccessToken(activeUser)).thenReturn("access_token");
        when(jwtService.generateRefreshToken(activeUser)).thenReturn("refresh_token");

        final AuthenticationResponseDto result = useCase.execute(requestWithEmail);

        assertEquals("access_token", result.accessToken());
        assertEquals("refresh_token", result.refreshToken());

        verify(authenticationService).authenticate("mail@example.com", "password");
        verify(jwtService).generateAccessToken(activeUser);
        verify(jwtService).generateRefreshToken(activeUser);
        verify(jwtService).saveUserToken(
                eq("access_token"),
                eq("refresh_token"),
                eq(activeUser),
                any(UUID.class));
        verify(auditService).logAudit(
                eq(activeUser.getId()),
                eq("AUTHENTICATE"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenBadCredentials_whenAuthenticate_thenThrowsBadCredentialsException() {
        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationService).authenticate("username", "password");

        final BadCredentialsException ex = assertThrows(BadCredentialsException.class,
                () -> useCase.execute(requestWithUsername));

        assertEquals("Invalid credentials", ex.getMessage());
        verify(userRepository, never()).findByUsernameOrEmail(any());
        verify(jwtService, never()).generateAccessToken(any());
    }

    @Test
    void givenLockedUser_whenAuthenticate_thenThrowsLockedException() {
        doThrow(new LockedException("User is locked"))
                .when(authenticationService)
                .authenticate("username", "password");

        final LockedException ex = assertThrows(LockedException.class,
                () -> useCase.execute(requestWithUsername));

        assertEquals("User is locked", ex.getMessage());
        verify(jwtService, never()).generateAccessToken(any());
        verify(auditService, never()).logAudit(any(), any(), any(), any());
    }

    @Test
    void givenDeletedUser_whenAuthenticate_thenThrowsAccountDeletedException() {
        doThrow(new AccountDeletedException("User is deleted"))
                .when(authenticationService)
                .authenticate("username", "password");

        final AccountDeletedException ex = assertThrows(AccountDeletedException.class,
                () -> useCase.execute(requestWithUsername));

        assertEquals("User is deleted", ex.getMessage());
        verify(jwtService, never()).generateAccessToken(any());
        verify(auditService, never()).logAudit(any(), any(), any(), any());
    }

    @Test
    void givenUserNotFound_whenAuthenticate_thenThrowsUsernameNotFoundException() {
        when(userRepository.findByUsernameOrEmail("username")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> useCase.execute(requestWithUsername));

        verify(jwtService, never()).generateAccessToken(any());
        verify(auditService, never()).logAudit(any(), any(), any(), any());
    }
}