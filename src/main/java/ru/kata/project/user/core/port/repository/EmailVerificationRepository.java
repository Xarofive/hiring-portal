package ru.kata.project.user.core.port.repository;

import ru.kata.project.user.core.entity.EmailVerification;

import java.util.Optional;

/**
 * EmailVerificationRepository
 * <p>
 * Порт для взаимодействия с сущностями подтверждения регистрации пользователя {@link EmailVerification}.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface EmailVerificationRepository {

    EmailVerification save(EmailVerification emailVerification);

    Optional<EmailVerification> findByCodeHash(String codeHash);
}
