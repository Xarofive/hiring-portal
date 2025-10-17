package ru.kata.project.user.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * AuthAudit
 * <p>
 * Представляет запись аудита действий пользователя в системе.
 * Содержит информацию о том, какой пользователь совершил какое событие, с какого IP и когда.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthAudit {
    private Long id;
    private UUID userId;
    private String eventType;
    private String ip;
    private String metadataJson;
    private Timestamp createdAt;
}
