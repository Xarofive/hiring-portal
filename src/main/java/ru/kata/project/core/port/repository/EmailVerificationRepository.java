package ru.kata.project.core.port.repository;

import ru.kata.project.core.entity.EmailVerification;

import java.util.Optional;

public interface EmailVerificationRepository {
    EmailVerification save(EmailVerification emailVerification);

    Optional<EmailVerification> findByCodeHash(String codeHash);

}
