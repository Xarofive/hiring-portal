package ru.kata.project.user.core.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * EmailResetDto
 * <p>
 * DTO-класс, представляющий email пользователя, у которого необходимо сбросить пароль.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record EmailResetDto(
        String email
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 4741732424147742459L;
}