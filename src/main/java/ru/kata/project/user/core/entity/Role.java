package ru.kata.project.user.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

/**
 * Role
 * <p>
 * Представляет роль для определённых действий в системе.
 * Конкретные действия определяются разрешениями, выданными для роли {@link Permission}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Role {
    private UUID id;
    private String code;
    private String name;
    private Set<Permission> permissions;
}
