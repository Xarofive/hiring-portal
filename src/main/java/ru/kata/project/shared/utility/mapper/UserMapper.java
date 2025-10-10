package ru.kata.project.shared.utility.mapper;

import ru.kata.project.core.entity.User;
import ru.kata.project.persistence.entity.UserEntity;

public class UserMapper {
    public static User toDomain(UserEntity entity) {
        return entity == null ? null : User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .roles(RoleMapper.toDomainSet(entity.getRoles()))
                .emailVerifiedAt(entity.getEmailVerifiedAt())
                .failedLoginCount(entity.getFailedLoginCount())
                .lastLoginAt(entity.getLastLoginAt())
                .lockedUntil(entity.getLockedUntil())
                .status(entity.getStatus())
                .userProfile(UserProfileMapper.toDomain(entity.getUserProfile()))
                .mfaEnabled(entity.isMfaEnabled())
                .mfaSecret(entity.getMfaSecret())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .passwordUpdatedAt(entity.getPasswordUpdatedAt())
                .build();
    }

    public static UserEntity toEntity(User domain) {
        return domain == null ? null : UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .passwordHash(domain.getPasswordHash())
                .roles(RoleMapper.toEntitySet(domain.getRoles()))
                .emailVerifiedAt(domain.getEmailVerifiedAt())
                .failedLoginCount(domain.getFailedLoginCount())
                .lastLoginAt(domain.getLastLoginAt())
                .lockedUntil(domain.getLockedUntil())
                .status(domain.getStatus())
                .userProfile(UserProfileMapper.toEntity(domain.getUserProfile()))
                .mfaEnabled(domain.isMfaEnabled())
                .mfaSecret(domain.getMfaSecret())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .passwordUpdatedAt(domain.getPasswordUpdatedAt())
                .build();
    }
}
