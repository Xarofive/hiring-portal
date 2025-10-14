package ru.kata.project.user.persistence.repository.inmemory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.port.repository.UserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserRepositoryInMemory
 * <p>
 * Реализация in-memory репозитория {@link UserRepository}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Repository
@Primary
public class UserRepositoryInMemory implements UserRepository {

    private final Map<UUID, User> storage = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return storage.values().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return storage.values().stream()
                .filter(u -> u.getId().equals(uuid))
                .findFirst();
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return usernameOrEmail.contains("@") ? findByEmail(usernameOrEmail) : findByUsername(usernameOrEmail);
    }

    @Override
    public boolean existsByUsername(String username) {
        return storage.values().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return storage.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
