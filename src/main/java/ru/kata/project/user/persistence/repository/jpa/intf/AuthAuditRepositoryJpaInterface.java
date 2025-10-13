package ru.kata.project.user.persistence.repository.jpa.intf;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.project.user.persistence.entity.AuthAuditEntity;

import java.util.UUID;

public interface AuthAuditRepositoryJpaInterface extends JpaRepository<AuthAuditEntity, UUID> {
}
