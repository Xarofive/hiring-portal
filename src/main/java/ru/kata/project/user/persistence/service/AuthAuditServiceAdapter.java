package ru.kata.project.user.persistence.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kata.project.user.core.entity.AuthAudit;
import ru.kata.project.user.core.port.repository.AuthAuditRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * AuthAuditService
 * <p>
 * Сервис аудита действий пользователя в системе.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Service
@RequiredArgsConstructor
public class AuthAuditServiceAdapter implements AuthAuditService {
    private final AuthAuditRepository auditRepository;

    @Override
    public void logAudit(UUID userId, String eventType, String ip, String metadataJson) {
        final AuthAudit audit = new AuthAudit();
        audit.setUserId(userId);
        audit.setEventType(eventType);
        audit.setIp(ip);
        audit.setMetadataJson(metadataJson);
        audit.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        auditRepository.save(audit);
    }

    @Override
    public List<AuthAudit> findAll() {
        return auditRepository.findAll();
    }
}
