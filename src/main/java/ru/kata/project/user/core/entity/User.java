package ru.kata.project.user.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kata.project.user.shared.utility.enumeration.UserStatus;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

/**
 * User
 * <p>
 * Представляет пользователя и используется для авторизации в системе.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    private UUID id;
    private String username;
    private String email;
    private String passwordHash;
    private String mfaSecret;
    private UserStatus status;
    private Set<Role> roles;
    private boolean mfaEnabled;
    private int failedLoginCount;
    private Timestamp emailVerifiedAt;
    private Timestamp lockedUntil;
    private Timestamp lastLoginAt;
    private Timestamp passwordUpdatedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private UserProfile userProfile;
}
