package ru.kata.project.user.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Permission
 * <p>
 * Представляет разрешение для определённого действия в системе.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Permission {
    private UUID id;
    private String code;
    private String description;
}
