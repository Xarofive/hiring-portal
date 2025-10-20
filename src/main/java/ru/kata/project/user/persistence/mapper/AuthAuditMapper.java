package ru.kata.project.user.persistence.mapper;

import ru.kata.project.user.core.entity.AuthAudit;
import ru.kata.project.user.persistence.entity.AuthAuditEntity;

import java.util.List;
import java.util.stream.Collectors;

public class AuthAuditMapper {

    public static AuthAudit toDomain(AuthAuditEntity entity) {
        if (entity == null) {
            return null;
        }
        return AuthAudit.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .eventType(entity.getEventType())
                .createdAt(entity.getCreatedAt())
                .ip(entity.getIp())
                .metadataJson(entity.getMetadataJson())
                .build();
    }

    public static AuthAuditEntity toEntity(AuthAudit domain) {
        if (domain == null) {
            return null;
        }
        return AuthAuditEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .eventType(domain.getEventType())
                .createdAt(domain.getCreatedAt())
                .ip(domain.getIp())
                .metadataJson(domain.getMetadataJson())
                .build();
    }

    public static List<AuthAudit> toDomainList(List<AuthAuditEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(AuthAuditMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static List<AuthAuditEntity> toEntityList(List<AuthAudit> domains) {
        if (domains == null) {
            return null;
        }
        return domains.stream()
                .map(AuthAuditMapper::toEntity)
                .collect(Collectors.toList());
    }
}
