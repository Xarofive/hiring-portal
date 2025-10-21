package ru.kata.project.user.persistence.mapper;

import ru.kata.project.user.core.entity.UserProfile;
import ru.kata.project.user.persistence.entity.UserProfileEntity;

public class UserProfileMapper {

    public static UserProfile toDomain(UserProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserProfile.builder()
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
        if (domain == null) {
            return null;
        }
        return UserProfileEntity.builder()
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