package ru.kata.project.user.core.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.port.repository.TokenRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.service.JwtService;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для use-case "Выход пользователя из системы" {@link LogoutUserUseCase}
 *
 * @author Vladislav_Bogomolov
 */
@ExtendWith(MockitoExtension.class)
class LogoutUserUseCaseTest {

    @Mock
    TokenRepository tokenRepository;
    @Mock
    JwtService jwtService;
    @Mock
    AuthAuditService auditService;

    @InjectMocks
    LogoutUserUseCase useCase;

    private Token token;
    private String refreshToken;

    @BeforeEach
    void setUp() {
        refreshToken = "refresh_token";
        token = Token.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .familyId(UUID.randomUUID())
                .refreshToken(refreshToken)
                .build();
    }

    @Test
    void givenValidRefreshToken_whenLogout_thenRevokeFamilyAndLogAudit() {
        when(tokenRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(token));

        useCase.execute(refreshToken);

        verify(jwtService).revokeFamily(token.getFamilyId());
        verify(auditService).logAudit(
                eq(token.getUserId()),
                eq("LOGOUT"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenNonExistentRefreshToken_whenLogout_thenDoNothing() {
        when(tokenRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.empty());

        useCase.execute(refreshToken);

        verify(jwtService, never()).revokeFamily(any());
        verify(auditService, never()).logAudit(any(), any(), any(), any());
    }

    @Test
    void givenNullRefreshToken_whenLogout_thenDoNothing() {
        useCase.execute(null);

        verify(tokenRepository, never()).findByRefreshToken(any());
        verify(jwtService, never()).revokeFamily(any());
        verify(auditService, never()).logAudit(any(), any(), any(), any());
    }
}