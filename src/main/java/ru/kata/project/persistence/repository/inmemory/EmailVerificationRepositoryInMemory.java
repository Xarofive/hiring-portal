package ru.kata.project.persistence.repository.inmemory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.kata.project.core.entity.EmailVerification;
import ru.kata.project.core.port.repository.EmailVerificationRepository;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class EmailVerificationRepositoryInMemory implements EmailVerificationRepository {

    private final Map<UUID, EmailVerification> storage = new ConcurrentHashMap<>();

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        if (emailVerification.getId() == null) {
            emailVerification.setId(UUID.randomUUID());
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