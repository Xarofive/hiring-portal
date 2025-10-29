package ru.kata.project.user.core.dto;

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
public record EmailVerificationCodeDto(
        String code
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -7451721119215372442L;
}