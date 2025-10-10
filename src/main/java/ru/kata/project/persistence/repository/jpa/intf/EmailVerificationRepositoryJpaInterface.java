package ru.kata.project.persistence.repository.jpa.intf;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.project.persistence.entity.EmailVerificationEntity;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepositoryJpaInterface extends JpaRepository<EmailVerificationEntity, UUID> {
    Optional<EmailVerificationEntity> findByCodeHash(String codeHash);
}
