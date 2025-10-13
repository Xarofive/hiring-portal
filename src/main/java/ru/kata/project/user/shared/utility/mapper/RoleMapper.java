package ru.kata.project.user.shared.utility.mapper;

import ru.kata.project.user.core.entity.Role;
import ru.kata.project.user.persistence.entity.RoleEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {
    public static Role toDomain(RoleEntity entity) {
        return entity == null ? null : Role.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .permissions(PermissionMapper.toDomainSet(entity.getPermissions()))
                .build();
    }

    public static RoleEntity toEntity(Role domain) {
        return domain == null ? null : RoleEntity.builder()
                .id(domain.getId())
                .code(domain.getCode())
                .name(domain.getName())
                .permissions(PermissionMapper.toEntitySet(domain.getPermissions()))
                .build();
    }

    public static Set<Role> toDomainSet(Set<RoleEntity> entities) {
        return entities == null ? null : entities.stream()
                .map(RoleMapper::toDomain)
                .collect(Collectors.toSet());
    }

    public static Set<RoleEntity> toEntitySet(Set<Role> roles) {
        return roles == null ? null : roles.stream()
                .map(RoleMapper::toEntity)
                .collect(Collectors.toSet());
    }

}
