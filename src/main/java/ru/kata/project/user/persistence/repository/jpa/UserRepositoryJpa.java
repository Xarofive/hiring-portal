package ru.kata.project.user.persistence.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.persistence.repository.jpa.intf.UserRepositoryJpaInterface;
import ru.kata.project.user.utility.mapper.UserMapper;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepositoryJpa
 * <p>
 * Реализация JPA репозитория {@link UserRepository}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryJpa implements UserRepository {

    private final UserRepositoryJpaInterface userRepository;

    @Override
    public User save(User user) {
        return UserMapper.toDomain(userRepository.save(UserMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return userRepository.findById(uuid).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            return userRepository.findByEmail(usernameOrEmail).map(UserMapper::toDomain);
        }
        return userRepository.findByUsername(usernameOrEmail).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
