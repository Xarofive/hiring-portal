package ru.kata.project.shared.utility.mapper;

import ru.kata.project.core.entity.Permission;
import ru.kata.project.persistence.entity.PermissionEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class PermissionMapper {

    public static Permission toDomain(PermissionEntity entity) {
        return entity == null ? null : Permission.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .description(entity.getDescription())
                .build();
    }

    public static PermissionEntity toEntity(Permission domain) {
        return domain == null ? null : PermissionEntity.builder()
                .id(domain.getId())
                .code(domain.getCode())
                .description(domain.getDescription())
                .build();
    }

    public static Set<Permission> toDomainSet(Set<PermissionEntity> entities) {
        return entities == null ? null : entities.stream()
                .map(PermissionMapper::toDomain)
                .collect(Collectors.toSet());
    }

    public static Set<PermissionEntity> toEntitySet(Set<Permission> domains) {
        return domains == null ? null : domains.stream()
                .map(PermissionMapper::toEntity)
                .collect(Collectors.toSet());
    }
}
