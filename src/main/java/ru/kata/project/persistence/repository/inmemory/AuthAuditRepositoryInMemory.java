package ru.kata.project.persistence.repository.inmemory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.kata.project.core.entity.AuthAudit;
import ru.kata.project.core.port.repository.AuthAuditRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Primary
public class AuthAuditRepositoryInMemory implements AuthAuditRepository {

    private final List<AuthAudit> storage = new CopyOnWriteArrayList<>();

    @Override
    public AuthAudit save(AuthAudit audit) {
        if (audit.getId() == null) {
            audit.setId(UUID.randomUUID());
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