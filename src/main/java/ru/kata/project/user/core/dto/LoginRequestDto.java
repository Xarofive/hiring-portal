package ru.kata.project.user.core.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * LoginRequestDto
 * <p>
 * DTO-класс, представляющий данные для входа пользователя в систему.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record LoginRequestDto(
        String username,
        String password
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -2602037526247606250L;
}