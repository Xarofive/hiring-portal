package ru.kata.project.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoginRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2602037526247606250L;
    private String username;
    private String password;
}