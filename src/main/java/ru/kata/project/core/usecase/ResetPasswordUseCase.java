package ru.kata.project.core.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.project.core.dto.NewPasswordCoreDto;
import ru.kata.project.core.entity.EmailVerification;
import ru.kata.project.core.entity.User;
import ru.kata.project.core.port.EmailCodeEncoder;
import ru.kata.project.core.port.UserPasswordEncoder;
import ru.kata.project.core.port.repository.EmailVerificationRepository;
import ru.kata.project.core.port.repository.UserRepository;
import ru.kata.project.shared.security.service.AuthAuditService;
import ru.kata.project.shared.utility.enumeration.UserStatus;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class ResetPasswordUseCase {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final EmailCodeEncoder emailCodeEncoder;
    private final AuthAuditService auditService;

    public String execute(NewPasswordCoreDto newPasswordDto) {
        try {
            EmailVerification emailVerification = emailVerificationRepository
                    .findByCodeHash(emailCodeEncoder.encode(newPasswordDto.getCode())).get();
            User user = userRepository.findById(emailVerification.getUserId()).get();
            if (emailVerification.getConsumedAt() == null && user.getStatus().equals(UserStatus.LOCKED)) {
                user.setStatus(UserStatus.ACTIVE);
                user.setPasswordHash(passwordEncoder.encode(newPasswordDto.getPassword()));
                userRepository.save(user);
                emailVerification.setConsumedAt(new Timestamp(System.currentTimeMillis()));
                emailVerificationRepository.save(emailVerification);
                auditService.logAudit(user.getId(),
                        "RESET_PASSWORD", "0.0.0.0:0000", "{json:json}");
            } else {
                throw new RuntimeException("Код уже использован");
            }
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("Код недействителен");
        }
        return "Пароль успешно изменён, пользователь разблокирован";
    }
}
