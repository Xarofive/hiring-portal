package ru.kata.project.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthAudit {
    private UUID id;
    private UUID userId;
    private String eventType;
    private Timestamp createdAt;
    private String ip;
    private String metadataJson;
}
