package ru.kata.project.user.utility.mapper;

import ru.kata.project.user.core.entity.Permission;
import ru.kata.project.user.persistence.entity.PermissionEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class PermissionMapper {

    public static Permission toDomain(PermissionEntity entity) {
        if (entity == null) {
            return null;
        }
        return Permission.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .description(entity.getDescription())
                .build();
    }

    public static PermissionEntity toEntity(Permission domain) {
        if (domain == null) {
            return null;
        }
        return PermissionEntity.builder()
                .id(domain.getId())
                .code(domain.getCode())
                .description(domain.getDescription())
                .build();
    }

    public static Set<Permission> toDomainSet(Set<PermissionEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(PermissionMapper::toDomain)
                .collect(Collectors.toSet());
    }

    public static Set<PermissionEntity> toEntitySet(Set<Permission> domains) {
        if (domains == null) {
            return null;
        }
        return domains.stream()
                .map(PermissionMapper::toEntity)
                .collect(Collectors.toSet());
    }
}
