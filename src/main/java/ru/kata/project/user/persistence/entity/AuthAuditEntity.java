package ru.kata.project.user.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kata.project.user.core.entity.AuthAudit;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * AuthAuditEntity
 * <p>
 * Реализация {@link AuthAudit}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Entity
@Table(name = "auth_audit")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private UUID userId;
    private String eventType;
    private Timestamp createdAt;
    private String ip;
    private String userAgent;
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadataJson;
}