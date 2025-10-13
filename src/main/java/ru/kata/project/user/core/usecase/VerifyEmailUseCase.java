package ru.kata.project.user.core.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.project.user.core.entity.EmailVerification;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.port.EmailCodeEncoder;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.shared.dto.EmailVerificationCodeDto;
import ru.kata.project.user.shared.security.service.AuthAuditService;
import ru.kata.project.user.shared.utility.enumeration.UserStatus;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

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

    public String execute(EmailVerificationCodeDto codeDto) {
        try {
            EmailVerification emailVerification = emailVerificationRepository
                    .findByCodeHash(emailCodeEncoder.encode(codeDto.getCode()))
                    .orElseThrow(() -> new UsernameNotFoundException("Код недействителен"));
            if (emailVerification.getConsumedAt() == null) {
                User user = userRepository.findById(emailVerification.getUserId())
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
                user.setStatus(UserStatus.ACTIVE);
                emailVerification.setConsumedAt(new Timestamp(System.currentTimeMillis()));
                emailVerificationRepository.save(emailVerification);
                auditService.logAudit(user.getId(), "VERIFICATION", "0.0.0.0:0000", "{json:json}");
            } else {
                throw new RuntimeException("Код уже использован");
            }
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("Код недействителен");
        }
        return "Аккаунт подтверждён";
    }
}
