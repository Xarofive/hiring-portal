package ru.kata.project.user.core.port.service;

import ru.kata.project.user.core.entity.AuthAudit;

import java.util.List;
import java.util.UUID;

public interface AuthAuditService {
    void logAudit(UUID userId, String eventType, String ip, String metadataJson);

    List<AuthAudit> findAll();
}
