package ru.kata.project.user.shared.utility.mapper;

import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.persistence.entity.TokenEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TokenMapper {
    public static Token toDomain(TokenEntity entity) {
        return entity == null ? null : Token.builder()
                .id(entity.getId())
                .familyId(entity.getFamilyId())
                .accessToken(entity.getAccessToken())
                .refreshToken(entity.getRefreshToken())
                .revokedAt(entity.getRevokedAt())
                .issuedAt(entity.getIssuedAt())
                .expiresAt(entity.getExpiresAt())
                .ip(entity.getIp())
                .fingerprint(entity.getFingerprint())
                .loggedOut(entity.isLoggedOut())
                .userId(entity.getUserId())
                .build();
    }

    public static TokenEntity toEntity(Token domain) {
        return domain == null ? null : TokenEntity.builder()
                .id(domain.getId())
                .familyId(domain.getFamilyId())
                .accessToken(domain.getAccessToken())
                .refreshToken(domain.getRefreshToken())
                .revokedAt(domain.getRevokedAt())
                .issuedAt(domain.getIssuedAt())
                .expiresAt(domain.getExpiresAt())
                .ip(domain.getIp())
                .fingerprint(domain.getFingerprint())
                .loggedOut(domain.isLoggedOut())
                .userId(domain.getUserId())
                .build();
    }

    public static List<Token> toDomainList(List<TokenEntity> entities) {
        return entities == null ? null : entities.stream()
                .map(TokenMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static List<TokenEntity> toEntityList(List<Token> tokens) {
        return tokens == null ? null : tokens.stream()
                .map(TokenMapper::toEntity)
                .collect(Collectors.toList());
    }

}
