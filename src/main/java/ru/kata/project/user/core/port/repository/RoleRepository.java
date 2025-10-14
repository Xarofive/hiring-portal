package ru.kata.project.user.core.port.repository;

import ru.kata.project.user.core.entity.Role;

import java.util.Optional;

/**
 * RoleRepository
 * <p>
 * Порт для взаимодействия с ролями пользователя {@link Role}.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface RoleRepository {

    Optional<Role> findByCode(String code);

    boolean existsByCode(String code);
}
