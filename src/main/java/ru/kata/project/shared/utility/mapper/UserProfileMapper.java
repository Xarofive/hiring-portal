package ru.kata.project.shared.utility.mapper;

import ru.kata.project.core.entity.UserProfile;
import ru.kata.project.persistence.entity.UserProfileEntity;

public class UserProfileMapper {

    public static UserProfile toDomain(UserProfileEntity entity) {
        return entity == null ? null : UserProfile.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .locale(entity.getLocale())
                .timeZone(entity.getTimeZone())
                .phone(entity.getPhone())
                .headline(entity.getHeadline())
                .avatarUrl(entity.getAvatarUrl())
                .build();
    }

    public static UserProfileEntity toEntity(UserProfile domain) {
        return domain == null ? null : UserProfileEntity.builder()
                .id(domain.getId())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .locale(domain.getLocale())
                .timeZone(domain.getTimeZone())
                .phone(domain.getPhone())
                .headline(domain.getHeadline())
                .avatarUrl(domain.getAvatarUrl())
                .build();
    }
}