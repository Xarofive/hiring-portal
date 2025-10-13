package ru.kata.project.user.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kata.project.user.core.entity.Token;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * TokenEntity
 * <p>
 * Реализация {@link Token}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Entity
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TokenEntity {

    @Id
    private UUID id;
    private UUID familyId;
    private String accessToken;
    private String refreshToken;
    private Timestamp revokedAt;
    private Timestamp issuedAt;
    private Timestamp expiresAt;
    private String ip;
    private String fingerprint;
    private boolean loggedOut;
    private UUID userId;
}
