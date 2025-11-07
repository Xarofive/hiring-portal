package ru.kata.project.user.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.user.core.dto.EmailVerificationCodeDto;
import ru.kata.project.user.core.entity.EmailVerification;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserStatus;
import ru.kata.project.user.core.event.EmailVerifiedEvent;
import ru.kata.project.user.core.exception.InvalidCodeException;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.service.OutboxUserEventPublisher;
import ru.kata.project.user.core.port.utility.EmailCodeEncoder;

import java.sql.Timestamp;

/**
 * VerifyEmailUseCase
 * <p>
 * Use-case для обработки сценария "Подтверждение регистрации пользователя".
 * </p>
 * <p>
 * Отвечает за подтверждение регистрации нового пользователя
 * </p>
 * <ul>
 *  <li> сохранение данных нового пользователя через {@link UserRepository};</li>
 *  <li> сохранение верификационного кода через {@link EmailVerificationRepository};</li>
 *  <li> хеширование верификационного кода через {@link EmailCodeEncoder};</li>
 *  <li> логирование события "VERIFICATION" через {@link AuthAuditService}.</li>
 *  <li> публикация в Kafka через {@link OutboxUserEventPublisher}.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@RequiredArgsConstructor
public class VerifyEmailUseCase {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailCodeEncoder emailCodeEncoder;
    private final AuthAuditService auditService;
    private final OutboxUserEventPublisher outboxUserEventPublisher;

    public String execute(EmailVerificationCodeDto codeDto) {
        final EmailVerification emailVerification = emailVerificationRepository
                .findByCodeHash(emailCodeEncoder.encode(codeDto.code()))
                .orElseThrow(() -> new InvalidCodeException("Код недействителен"));
        if (emailVerification.getConsumedAt() == null) {
            final User user = userRepository.findById(emailVerification.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
            user.setStatus(UserStatus.ACTIVE);
            emailVerification.setConsumedAt(new Timestamp(System.currentTimeMillis()));
            emailVerificationRepository.save(emailVerification);
            auditService.logAudit(user.getId(), "VERIFICATION", "0.0.0.0:0000", "{json:json}");
            publishEmailVerifiedEvent(user);
        } else {
            throw new InvalidCodeException("Код уже использован");
        }
        return "Аккаунт подтверждён";
    }

    private void publishEmailVerifiedEvent(User user) {
        final var event = EmailVerifiedEvent.of(
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
