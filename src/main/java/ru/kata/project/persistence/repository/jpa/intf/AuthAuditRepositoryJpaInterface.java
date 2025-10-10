package ru.kata.project.persistence.repository.jpa.intf;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.project.persistence.entity.AuthAuditEntity;

import java.util.UUID;

public interface AuthAuditRepositoryJpaInterface extends JpaRepository<AuthAuditEntity, UUID> {
}
