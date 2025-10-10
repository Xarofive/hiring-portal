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
public class Token {
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
