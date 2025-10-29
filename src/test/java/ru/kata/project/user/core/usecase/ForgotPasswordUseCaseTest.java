package ru.kata.project.user.core.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.user.core.dto.EmailResetDto;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserStatus;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.TokenRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.service.JwtService;
import ru.kata.project.user.core.port.utility.EmailCodeEncoder;
import ru.kata.project.user.core.port.utility.EmailCodeGenerator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для use-case "Забыли пароль" {@link ForgotPasswordUseCase}
 *
 * @author Vladislav_Bogomolov
 */
@ExtendWith(MockitoExtension.class)
class ForgotPasswordUseCaseTest {

    @Mock
    UserRepository userRepository;
    @Mock
    EmailVerificationRepository emailVerificationRepository;
    @Mock
    TokenRepository tokenRepository;
    @Mock
    EmailCodeGenerator emailCodeGenerator;
    @Mock
    EmailCodeEncoder emailCodeEncoder;
    @Mock
    JwtService jwtService;
    @Mock
    AuthAuditService auditService;

    @InjectMocks
    ForgotPasswordUseCase useCase;

    private User activeUser;
    private EmailResetDto emailDto;
    private String generatedCode;
    private Token token1, token2;

    @BeforeEach
    void setUp() {
        activeUser = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .email("mail@example.com")
                .passwordHash("old_hash")
                .status(UserStatus.ACTIVE)
                .build();

        emailDto = new EmailResetDto(activeUser.getEmail());

        generatedCode = "123456";

        token1 = Token.builder()
                .id(UUID.randomUUID())
                .familyId(UUID.randomUUID())
                .userId(activeUser.getId())
                .build();

        token2 = Token.builder()
                .id(UUID.randomUUID())
                .familyId(UUID.randomUUID())
                .userId(activeUser.getId())
                .build();
    }

    @Test
    void givenExistingUser_whenForgotPassword_thenBlockUserAndGenerateEmailCodeAndRevokeTokens() {
        when(userRepository.findByEmail(activeUser.getEmail())).thenReturn(Optional.of(activeUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(emailCodeGenerator.generate()).thenReturn(generatedCode);
        when(emailCodeEncoder.encode(generatedCode)).thenReturn("encoded123456");
        when(tokenRepository.findAllByUserId(activeUser.getId())).thenReturn(List.of(token1, token2));

        final String expected = "Пароль сброшен, аккаунт заблокирован. Код для установки нового пароля - " + generatedCode;
        final String actual = useCase.execute(emailDto);

        assertEquals(expected, actual);
        assertEquals(UserStatus.LOCKED, activeUser.getStatus());
        assertEquals("", activeUser.getPasswordHash());

        verify(emailVerificationRepository).save(any());
        verify(jwtService).revokeToken(token1);
        verify(jwtService).revokeToken(token2);
        verify(jwtService).revokeFamily(token1.getFamilyId());
        verify(jwtService).revokeFamily(token2.getFamilyId());
        verify(auditService).logAudit(
                eq(activeUser.getId()),
                eq("FORGOT_PASSWORD"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenNonExistentUser_whenForgotPassword_thenThrowsUserNotFoundException() {
        when(userRepository.findByEmail(activeUser.getEmail())).thenReturn(Optional.empty());

        final UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> useCase.execute(emailDto));

        assertEquals("Пользователь не может быть заблокирован", ex.getMessage());
    }
}