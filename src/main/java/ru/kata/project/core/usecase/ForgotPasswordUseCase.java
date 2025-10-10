package ru.kata.project.core.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.project.core.dto.EmailResetCoreDto;
import ru.kata.project.core.entity.EmailVerification;
import ru.kata.project.core.entity.Token;
import ru.kata.project.core.entity.User;
import ru.kata.project.core.port.EmailCodeEncoder;
import ru.kata.project.core.port.EmailCodeGenerator;
import ru.kata.project.core.port.repository.EmailVerificationRepository;
import ru.kata.project.core.port.repository.TokenRepository;
import ru.kata.project.core.port.repository.UserRepository;
import ru.kata.project.shared.security.service.AuthAuditService;
import ru.kata.project.shared.security.service.JwtService;
import ru.kata.project.shared.utility.enumeration.UserStatus;

import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
public class ForgotPasswordUseCase {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final TokenRepository tokenRepository;
    private final EmailCodeGenerator emailCodeGenerator;
    private final EmailCodeEncoder emailCodeEncoder;
    private final JwtService jwtService;
    private final AuthAuditService auditService;

    public String execute(EmailResetCoreDto email) {
        String token;
        try {
            User user = userRepository.findByEmail(email.getEmail()).get();
            user.setStatus(UserStatus.LOCKED);
            user.setPasswordHash("");
            userRepository.save(user);
            token = emailCodeGenerator.generate();
            emailVerificationRepository.save(EmailVerification.builder()
                    .userId(user.getId())
                    .codeHash(emailCodeEncoder.encode(token))
                    .build());
            tokenRepository.findAllByUserId(user.getId()).forEach(jwtService::revokeToken);
            tokenRepository.findAllByUserId(user.getId()).stream()
                    .map(Token::getFamilyId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .forEach(jwtService::revokeFamily);
            auditService.logAudit(user.getId(), "FORGOT_PASSWORD", "0.0.0.0:0000", "{json:json}");
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("Код недействителен");
        }
        return "Пароль сброшен, аккаунт заблокирован. Код для установки нового пароля - " + token;
    }
}
