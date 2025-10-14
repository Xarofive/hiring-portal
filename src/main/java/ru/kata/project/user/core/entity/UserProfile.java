package ru.kata.project.user.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * UserProfile
 * <p>
 * Представляет личные данные пользователя {@link User}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserProfile {
    private UUID id;
    private String firstName;
    private String lastName;
    private String locale;
    private String timeZone;
    private String phone;
    private String headline;
    private String avatarUrl;
}
