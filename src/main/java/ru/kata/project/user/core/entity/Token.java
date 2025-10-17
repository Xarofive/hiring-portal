package ru.kata.project.user.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Token
 * <p>
 * Представляет токены для авторизации в системе.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Token {
    private UUID id;
    private UUID familyId;
    private UUID userId;
    private String accessToken;
    private String refreshToken;
    private String ip;
    private String fingerprint;
    private Timestamp revokedAt;
    private Timestamp issuedAt;
    private Timestamp expiresAt;
    private boolean loggedOut;
}
