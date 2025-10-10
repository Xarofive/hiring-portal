package ru.kata.project.core.port.repository;

import ru.kata.project.core.entity.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByCode(String code);

    boolean existsByCode(String code);
}
