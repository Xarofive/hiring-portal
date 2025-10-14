package ru.kata.project.user.core.port.repository;

import ru.kata.project.user.core.entity.UserProfile;

import java.util.Optional;

/**
 * UserProfileRepository
 * <p>
 * Порт для взаимодействия с личными данными пользователя {@link UserProfile}.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface UserProfileRepository {

    UserProfile save(UserProfile userProfile);

    Optional<UserProfile> findById(Long id);
}
