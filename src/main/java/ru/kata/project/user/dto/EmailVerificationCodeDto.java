package ru.kata.project.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * EmailVerificationCodeDto
 * <p>
 * DTO-класс, представляющий код, который отправляет пользователь для успешного подтверждения регистрации.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EmailVerificationCodeDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -7451721119215372442L;
    private String code;
}