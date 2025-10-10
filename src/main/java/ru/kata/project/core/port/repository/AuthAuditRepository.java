package ru.kata.project.core.port.repository;

import ru.kata.project.core.entity.AuthAudit;

import java.util.List;

public interface AuthAuditRepository {
    AuthAudit save(AuthAudit audit);

    List<AuthAudit> findAll();
}
