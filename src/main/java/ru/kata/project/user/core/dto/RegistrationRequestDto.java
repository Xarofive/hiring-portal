package ru.kata.project.user.core.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * RegistrationRequestDto
 * <p>
 * DTO-класс, представляющий данные для регистрации нового пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record RegistrationRequestDto(
        String username,
        String email,
        String password,
        String firstName,
        String lastName
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -5570002741852687592L;
}