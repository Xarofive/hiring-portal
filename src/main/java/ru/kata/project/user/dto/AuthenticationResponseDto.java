package ru.kata.project.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthenticationResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2002427478722949479L;
    private String accessToken;
    private String refreshToken;
}