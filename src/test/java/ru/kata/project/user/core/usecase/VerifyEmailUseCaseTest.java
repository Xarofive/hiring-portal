package ru.kata.project.user.core.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.user.core.dto.EmailVerificationCodeDto;
import ru.kata.project.user.core.entity.EmailVerification;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserStatus;
import ru.kata.project.user.core.exception.InvalidCodeException;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.utility.EmailCodeEncoder;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для use-case "Подтверждение регистрации пользователя" {@link VerifyEmailUseCase}
 *
 * @author Vladislav_Bogomolov
 */
@ExtendWith(MockitoExtension.class)
class VerifyEmailUseCaseTest {

    @Mock
    UserRepository userRepository;
    @Mock
    EmailVerificationRepository emailVerificationRepository;
    @Mock
    EmailCodeEncoder emailCodeEncoder;
    @Mock
    AuthAuditService auditService;

    @InjectMocks
    VerifyEmailUseCase useCase;

    private User user;
    private EmailVerification verification;
    private EmailVerificationCodeDto codeDto;
    private String code;

    @BeforeEach
    void setUp() {
        code = "123456";
        user = User.builder()
                .id(UUID.randomUUID())
                .status(UserStatus.PENDING_EMAIL)
                .build();
        verification = EmailVerification.builder()
                .userId(user.getId())
                .codeHash("encoded_code")
                .consumedAt(null)
                .build();
        codeDto = new EmailVerificationCodeDto(code);
    }

    @Test
    void givenValidCode_whenVerifyEmail_thenVerifyUserAndConsumeCode() {
        when(emailCodeEncoder.encode(code)).thenReturn("encoded_code");
        when(emailVerificationRepository.findByCodeHash("encoded_code")).thenReturn(Optional.of(verification));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(emailVerificationRepository.save(verification)).thenAnswer(invocation -> invocation.getArgument(0));

        final String result = useCase.execute(codeDto);

        assertEquals("Аккаунт подтверждён", result);
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertNotNull(verification.getConsumedAt());

        verify(emailVerificationRepository).save(verification);
        verify(auditService).logAudit(
                eq(user.getId()),
                eq("VERIFICATION"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenNonExistentCode_whenVerifyEmail_thenThrowsInvalidCodeException() {
        when(emailCodeEncoder.encode(code)).thenReturn("encoded_code");
        when(emailVerificationRepository.findByCodeHash("encoded_code")).thenReturn(Optional.empty());

        final InvalidCodeException ex = assertThrows(InvalidCodeException.class,
                () -> useCase.execute(codeDto));
        assertEquals("Код недействителен", ex.getMessage());
    }

    @Test
    void givenAlreadyConsumedCode_whenVerifyEmail_thenThrowsInvalidCodeException() {
        verification.setConsumedAt(new Timestamp(System.currentTimeMillis()));
        when(emailCodeEncoder.encode(code)).thenReturn("encoded_code");
        when(emailVerificationRepository.findByCodeHash("encoded_code")).thenReturn(Optional.of(verification));

        final InvalidCodeException ex = assertThrows(InvalidCodeException.class,
                () -> useCase.execute(codeDto));
        assertEquals("Код уже использован", ex.getMessage());
    }

    @Test
    void givenUserNotFound_whenVerifyEmail_thenThrowsUserNotFoundException() {
        when(emailCodeEncoder.encode(code)).thenReturn("encoded_code");
        when(emailVerificationRepository.findByCodeHash("encoded_code")).thenReturn(Optional.of(verification));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        final UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> useCase.execute(codeDto));
        assertEquals("Пользователь не найден", ex.getMessage());
    }
}
