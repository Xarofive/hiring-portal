package ru.kata.project.user.core.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.user.core.dto.NewPasswordDto;
import ru.kata.project.user.core.entity.EmailVerification;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserStatus;
import ru.kata.project.user.core.exception.InvalidCodeException;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.utility.EmailCodeEncoder;
import ru.kata.project.user.core.port.utility.UserPasswordEncoder;

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
 * Unit-тесты для use-case "Сброс и установка пароля" {@link ResetPasswordUseCase}
 *
 * @author Vladislav_Bogomolov
 */
@ExtendWith(MockitoExtension.class)
class ResetPasswordUseCaseTest {

    @Mock
    UserRepository userRepository;
    @Mock
    EmailVerificationRepository emailVerificationRepository;
    @Mock
    UserPasswordEncoder passwordEncoder;
    @Mock
    EmailCodeEncoder emailCodeEncoder;
    @Mock
    AuthAuditService auditService;

    @InjectMocks
    ResetPasswordUseCase useCase;

    private User user;
    private EmailVerification verification;
    private NewPasswordDto dto;
    private String code;
    private String newPassword;

    @BeforeEach
    void setUp() {
        code = "123456";
        newPassword = "new_pass";
        user = User.builder()
                .id(UUID.randomUUID())
                .status(UserStatus.LOCKED)
                .build();
        verification = EmailVerification.builder()
                .userId(user.getId())
                .codeHash("encoded_code")
                .consumedAt(null)
                .build();
        dto = new NewPasswordDto(code, newPassword);
    }

    @Test
    void givenValidCodeAndUser_whenResetPassword_thenResetPasswordAndUnlockUser() {
        when(emailCodeEncoder.encode(code)).thenReturn("encoded_code");
        when(emailVerificationRepository.findByCodeHash("encoded_code")).thenReturn(Optional.of(verification));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("hashed_new_pass");
        when(userRepository.save(user)).thenAnswer(invocation -> invocation.getArgument(0));
        when(emailVerificationRepository.save(verification)).thenAnswer(invocation -> invocation.getArgument(0));

        final String result = useCase.execute(dto);

        assertEquals("Пароль успешно изменён, пользователь разблокирован", result);
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals("hashed_new_pass", user.getPasswordHash());
        assertNotNull(verification.getConsumedAt());

        verify(userRepository).save(user);
        verify(emailVerificationRepository).save(verification);
        verify(auditService).logAudit(
                eq(user.getId()),
                eq("RESET_PASSWORD"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenInvalidOrConsumedCode_whenResetPassword_thenThrowsInvalidCodeException() {
        when(emailCodeEncoder.encode(code)).thenReturn("encoded_code");
        when(emailVerificationRepository.findByCodeHash("encoded_code")).thenReturn(Optional.empty());

        final InvalidCodeException ex = assertThrows(InvalidCodeException.class,
                () -> useCase.execute(dto));
        assertEquals("Код недействителен или уже использован", ex.getMessage());
    }

    @Test
    void givenUserNotFound_whenResetPassword_thenThrowsUserNotFoundException() {
        when(emailCodeEncoder.encode(code)).thenReturn("encoded_code");
        when(emailVerificationRepository.findByCodeHash("encoded_code")).thenReturn(Optional.of(verification));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        final UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> useCase.execute(dto));
        assertEquals("Пользователь не найден", ex.getMessage());
    }

    @Test
    void givenUserNotLocked_whenResetPassword_thenThrowsRuntimeException() {
        user.setStatus(UserStatus.ACTIVE);
        when(emailCodeEncoder.encode(code)).thenReturn("encoded_code");
        when(emailVerificationRepository.findByCodeHash("encoded_code")).thenReturn(Optional.of(verification));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        final RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.execute(dto));
        assertEquals("Пользователь не заблокирован", ex.getMessage());
    }

    @Test
    void givenConsumedVerification_whenResetPassword_thenThrowsInvalidCodeException() {
        EmailVerification consumedVerification = EmailVerification.builder()
                .userId(UUID.randomUUID())
                .codeHash("encoded_code")
                .consumedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        when(emailVerificationRepository.findByCodeHash("encoded_code"))
                .thenReturn(Optional.of(consumedVerification));
        when(emailCodeEncoder.encode("code")).thenReturn("encoded_code");

        assertThrows(InvalidCodeException.class, () -> useCase.execute(
                new NewPasswordDto("code", "newPassword")));
    }
}
