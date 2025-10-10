package ru.kata.project.core.port.repository;

import ru.kata.project.core.entity.Permission;

import java.util.Optional;

public interface PermissionRepository {

    Optional<Permission> findByCode(String code);

    boolean existsByCode(String code);
}
