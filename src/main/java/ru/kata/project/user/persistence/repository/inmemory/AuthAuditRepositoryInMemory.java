package ru.kata.project.user.persistence.repository.inmemory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.kata.project.user.core.entity.AuthAudit;
import ru.kata.project.user.core.port.repository.AuthAuditRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AuthAuditRepositoryInMemory
 * <p>
 * Реализация in-memory репозитория {@link AuthAuditRepository}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Repository
@Primary
public class AuthAuditRepositoryInMemory implements AuthAuditRepository {

    private final List<AuthAudit> storage = new CopyOnWriteArrayList<>();

    private Long auditId = 0L;

    @Override
    public AuthAudit save(AuthAudit audit) {
        if (audit.getId() == null) {
            audit.setId(auditId += 1);
        }
        if (audit.getCreatedAt() == null) {
            audit.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        storage.add(audit);
        return audit;
    }

    @Override
    public List<AuthAudit> findAll() {
        return List.copyOf(storage);
    }
}