package ru.kata.project.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NewPasswordDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -6277481890153851332L;
    private String code;
    private String password;
}