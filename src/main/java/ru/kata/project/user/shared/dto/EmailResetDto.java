package ru.kata.project.user.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * EmailResetDto
 * <p>
 * DTO-класс, представляющий email пользователя, у которого необходимо сбросить пароль.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EmailResetDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4741732424147742459L;
    private String email;
}