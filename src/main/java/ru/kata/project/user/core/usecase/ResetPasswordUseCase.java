package ru.kata.project.user.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.user.core.dto.NewPasswordDto;
import ru.kata.project.user.core.entity.EmailVerification;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserStatus;
import ru.kata.project.user.core.event.PasswordResetEvent;
import ru.kata.project.user.core.exception.InvalidCodeException;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.service.OutboxUserEventPublisher;
import ru.kata.project.user.core.port.utility.EmailCodeEncoder;
import ru.kata.project.user.core.port.utility.UserPasswordEncoder;

import java.sql.Timestamp;

/**
 * ResetPasswordUseCase
 * <p>
 * Use-case для обработки сценария "Сброс и установка пароля".
 * </p>
 * <p>
 * Отвечает за сброс старого пароля, сохранение нового пароля и обновление статуса пользователя
 * </p>
 * <ul>
 *  <li> обновление данных пользователя через {@link UserRepository};</li>
 *  <li> поиск верификационного кода через {@link EmailVerificationRepository};</li>
 *  <li> хеширование верификационного кода через {@link EmailCodeEncoder};</li>
 *  <li> хеширование пароля через {@link UserPasswordEncoder};</li>
 *  <li> логирование события "RESET_PASSWORD" через {@link AuthAuditService}.</li>
 *  <li> публикация в Kafka через {@link OutboxUserEventPublisher}.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@RequiredArgsConstructor
public class ResetPasswordUseCase {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final EmailCodeEncoder emailCodeEncoder;
    private final AuthAuditService auditService;
    private final OutboxUserEventPublisher outboxUserEventPublisher;

    public String execute(NewPasswordDto newPasswordDto) {
        final EmailVerification verification = findEmailVerification(newPasswordDto.code());
        final User user = findUser(verification);
        resetPassword(user, verification, newPasswordDto.password());
        publishPasswordResetEvent(user);
        return "Пароль успешно изменён, пользователь разблокирован";
    }

    private EmailVerification findEmailVerification(String code) {
        return emailVerificationRepository
                .findByCodeHash(emailCodeEncoder.encode(code))
                .filter(v -> v.getConsumedAt() == null)
                .orElseThrow(() -> new InvalidCodeException("Код недействителен или уже использован"));
    }

    private User findUser(EmailVerification verification) {
        return userRepository.findById(verification.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    private void resetPassword(User user, EmailVerification verification, String newPassword) {
        if (!user.getStatus().equals(UserStatus.LOCKED)) {
            throw new RuntimeException("Пользователь не заблокирован");
        }
        user.setStatus(UserStatus.ACTIVE);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        verification.setConsumedAt(new Timestamp(System.currentTimeMillis()));
        emailVerificationRepository.save(verification);
        auditService.logAudit(user.getId(), "RESET_PASSWORD", "0.0.0.0:0000", "{json:json}");
    }

    private void publishPasswordResetEvent(User user) {
        final var event = PasswordResetEvent.of(
                user.getId(),
                user.getEmail()
        );

        outboxUserEventPublisher.publish(
                user.getId().toString(),
                event.eventType(),
                event,
                event.schemaVersion(),
                event.source(),
                event.traceId()
        );
    }
}
