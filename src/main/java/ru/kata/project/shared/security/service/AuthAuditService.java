package ru.kata.project.shared.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kata.project.core.entity.AuthAudit;
import ru.kata.project.core.port.repository.AuthAuditRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthAuditService {
    private final AuthAuditRepository auditRepository;

    public void logAudit(UUID userId, String eventType, String ip, String metadataJson) {
        AuthAudit audit = new AuthAudit();
        audit.setUserId(userId);
        audit.setEventType(eventType);
        audit.setIp(ip);
        audit.setMetadataJson(metadataJson);
        audit.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        auditRepository.save(audit);
    }

    public List<AuthAudit> findAll() {
        return auditRepository.findAll();
    }
}
