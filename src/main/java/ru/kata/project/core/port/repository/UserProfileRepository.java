package ru.kata.project.core.port.repository;

import ru.kata.project.core.entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepository {

    UserProfile save(UserProfile userProfile);

    Optional<UserProfile> findById(Long id);
}
