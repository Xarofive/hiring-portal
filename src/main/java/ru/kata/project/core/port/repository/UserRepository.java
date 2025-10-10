package ru.kata.project.core.port.repository;

import ru.kata.project.core.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID uuid);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
