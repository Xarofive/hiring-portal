package ru.kata.project.user.core.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * NewPasswordDto
 * <p>
 * DTO-класс, представляющий код для установки нового пароля и сам пароль.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */

public record NewPasswordDto(
        String code,
        String password
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -6277481890153851332L;
}