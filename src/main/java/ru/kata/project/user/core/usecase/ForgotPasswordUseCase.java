package ru.kata.project.user.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.user.core.dto.EmailResetDto;
import ru.kata.project.user.core.entity.EmailVerification;
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

import java.util.Objects;

/**
 * ForgotPasswordUseCase
 * <p>
 * Use-case для обработки сценария "Забыли пароль".
 * </p>
 * <p>
 * Отвечает за блокировку учетной записи пользователя, генерацию кода подтверждения
 * для восстановления пароля, аннулирование всех активных токенов и запись события аудита.
 * </p>
 * <ul>
 *  <li> поиск и блокировка пользователя через {@link UserRepository};</li>
 *  <li> генерация верификационного кода через {@link EmailCodeGenerator};</li>
 *  <li> хеширование верификационного кода через {@link EmailCodeEncoder};</li>
 *  <li> сохранение верификационного кода через {@link EmailVerificationRepository};</li>
 *  <li> обнуление действующих токенов через {@link JwtService};</li>
 *  <li> обновление информации о токенах через {@link TokenRepository};</li>
 *  <li> логирование события "FORGOT_PASSWORD" через {@link AuthAuditService}.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@RequiredArgsConstructor
public class ForgotPasswordUseCase {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final TokenRepository tokenRepository;
    private final EmailCodeGenerator emailCodeGenerator;
    private final EmailCodeEncoder emailCodeEncoder;
    private final JwtService jwtService;
    private final AuthAuditService auditService;

    public String execute(EmailResetDto email) {

        final User user = blockUser(email.email());

        final String token = generateEmailCode(user);

        revokeAllTokens(user);

        auditService.logAudit(user.getId(), "FORGOT_PASSWORD", "0.0.0.0:0000", "{json:json}");

        return "Пароль сброшен, аккаунт заблокирован. Код для установки нового пароля - " + token;
    }

    private User blockUser(String email) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не может быть заблокирован"));
        user.setStatus(UserStatus.LOCKED);
        user.setPasswordHash("");
        return userRepository.save(user);
    }

    private String generateEmailCode(User user) {
        final String token = emailCodeGenerator.generate();
        emailVerificationRepository.save(EmailVerification.builder()
                .userId(user.getId())
                .codeHash(emailCodeEncoder.encode(token))
                .build());
        return token;
    }

    private void revokeAllTokens(User user) {
        final var tokens = tokenRepository.findAllByUserId(user.getId());
        tokens.forEach(jwtService::revokeToken);
        tokens.stream()
                .map(Token::getFamilyId)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(jwtService::revokeFamily);
    }
}