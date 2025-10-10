package ru.kata.project.shared.utility.mapper;

import ru.kata.project.core.entity.EmailVerification;
import ru.kata.project.persistence.entity.EmailVerificationEntity;

public class EmailVerificationMapper {

    public static EmailVerification toDomain(EmailVerificationEntity entity) {
        return entity == null ? null : EmailVerification.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .codeHash(entity.getCodeHash())
                .consumedAt(entity.getConsumedAt())
                .build();
    }

    public static EmailVerificationEntity toEntity(EmailVerification domain) {
        return domain == null ? null : EmailVerificationEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .codeHash(domain.getCodeHash())
                .consumedAt(domain.getConsumedAt())
                .build();
    }
}
