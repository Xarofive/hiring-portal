package ru.kata.project.user.persistence.mapper;

import ru.kata.project.user.core.entity.Role;
import ru.kata.project.user.persistence.entity.RoleEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {
    public static Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return Role.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .permissions(PermissionMapper.toDomainSet(entity.getPermissions()))
                .build();
    }

    public static RoleEntity toEntity(Role domain) {
        if (domain == null) {
            return null;
        }
        return RoleEntity.builder()
                .id(domain.getId())
                .code(domain.getCode())
                .name(domain.getName())
                .permissions(PermissionMapper.toEntitySet(domain.getPermissions()))
                .build();
    }

    public static Set<Role> toDomainSet(Set<RoleEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(RoleMapper::toDomain)
                .collect(Collectors.toSet());
    }

    public static Set<RoleEntity> toEntitySet(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(RoleMapper::toEntity)
                .collect(Collectors.toSet());
    }
}
