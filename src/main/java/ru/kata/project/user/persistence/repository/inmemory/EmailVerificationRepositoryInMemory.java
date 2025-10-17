package ru.kata.project.user.persistence.repository.inmemory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.kata.project.user.core.entity.EmailVerification;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EmailVerificationRepositoryInMemory
 * <p>
 * Реализация in-memory репозитория {@link EmailVerificationRepository}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Repository
@Primary
public class EmailVerificationRepositoryInMemory implements EmailVerificationRepository {

    private final Map<Long, EmailVerification> storage = new ConcurrentHashMap<>();

    private Long emailVerificationId = 0L;

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        if (emailVerification.getId() == null) {
            emailVerification.setId(emailVerificationId += 1);
        }

        if (emailVerification.getConsumedAt() == null) {
            emailVerification.setConsumedAt(null);
        }

        storage.put(emailVerification.getId(), emailVerification);

        return emailVerification;
    }

    @Override
    public Optional<EmailVerification> findByCodeHash(String codeHash) {
        return storage.values().stream()
                .filter(v -> Objects.equals(v.getCodeHash(), codeHash))
                .findFirst();
    }
}