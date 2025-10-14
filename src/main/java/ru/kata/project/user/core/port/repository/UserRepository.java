package ru.kata.project.user.core.port.repository;

import ru.kata.project.user.core.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository
 * <p>
 * Порт для взаимодействия с аутентификационными данными пользователя {@link User}.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID uuid);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
