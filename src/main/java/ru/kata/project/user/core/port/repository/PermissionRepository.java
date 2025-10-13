package ru.kata.project.user.core.port.repository;

import ru.kata.project.user.core.entity.Permission;

import java.util.Optional;

/**
 * PermissionRepository
 * <p>
 * Порт для взаимодействия с разрешениями для пользователя {@link Permission}.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface PermissionRepository {

    Optional<Permission> findByCode(String code);

    boolean existsByCode(String code);
}
