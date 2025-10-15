package ru.kata.project.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RegistrationRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -5570002741852687592L;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}