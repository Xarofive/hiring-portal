package ru.kata.project.user.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * EmailVerification
 * <p>
 * Представляет запись о подтверждении регистрации пользователем.
 * Содержит информацию о том, какой пользователь и когда подтверждал почту.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EmailVerification {
    private Long id;
    private UUID userId;
    private String codeHash;
    private Timestamp consumedAt;
}
