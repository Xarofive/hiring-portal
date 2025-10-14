package ru.kata.project.user.persistence.repository.jpa.intf;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.project.user.persistence.entity.RoleEntity;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepositoryJpaInterface extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByCode(String code);

    boolean existsByCode(String code);
}
