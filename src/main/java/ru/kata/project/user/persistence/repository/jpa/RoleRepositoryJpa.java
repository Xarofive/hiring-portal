package ru.kata.project.user.persistence.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.kata.project.user.core.entity.Role;
import ru.kata.project.user.core.port.repository.RoleRepository;
import ru.kata.project.user.persistence.repository.jpa.intf.RoleRepositoryJpaInterface;
import ru.kata.project.user.utility.mapper.RoleMapper;

import java.util.Optional;

/**
 * RoleRepositoryJpa
 * <p>
 * Реализация JPA репозитория {@link RoleRepository}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryJpa implements RoleRepository {

    private final RoleRepositoryJpaInterface roleRepository;

    @Override
    public Optional<Role> findByCode(String code) {
        return roleRepository.findByCode(code).map(RoleMapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return roleRepository.existsByCode(code);
    }
}
