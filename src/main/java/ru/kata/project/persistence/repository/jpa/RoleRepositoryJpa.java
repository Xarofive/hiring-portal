package ru.kata.project.persistence.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.kata.project.core.entity.Role;
import ru.kata.project.core.port.repository.RoleRepository;
import ru.kata.project.persistence.repository.jpa.intf.RoleRepositoryJpaInterface;
import ru.kata.project.shared.utility.mapper.RoleMapper;

import java.util.Optional;

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
