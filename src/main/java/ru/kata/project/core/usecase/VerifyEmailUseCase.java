package ru.kata.project.core.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.project.core.dto.EmailVerificationCodeCoreDto;
import ru.kata.project.core.entity.EmailVerification;
import ru.kata.project.core.entity.User;
import ru.kata.project.core.port.EmailCodeEncoder;
import ru.kata.project.core.port.repository.EmailVerificationRepository;
import ru.kata.project.core.port.repository.UserRepository;
import ru.kata.project.shared.security.service.AuthAuditService;
import ru.kata.project.shared.utility.enumeration.UserStatus;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class VerifyEmailUseCase {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailCodeEncoder emailCodeEncoder;
    private final AuthAuditService auditService;

    public String execute(EmailVerificationCodeCoreDto codeDto) {
        try {
            EmailVerification emailVerification = emailVerificationRepository
                    .findByCodeHash(emailCodeEncoder.encode(codeDto.getCode())).get();
            if (emailVerification.getConsumedAt() == null) {
                User user = userRepository.findById(emailVerification.getUserId()).get();
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
