package ru.kata.project.user.core.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * AuthenticationResponseDto
 * <p>
 * DTO-класс, представляющий успешный ответ на запрос аутентификации пользователя в виде токенов.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record AuthenticationResponseDto(
        String accessToken,
        String refreshToken
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -2002427478722949479L;
}