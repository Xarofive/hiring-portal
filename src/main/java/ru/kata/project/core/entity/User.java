package ru.kata.project.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kata.project.shared.utility.enumeration.UserStatus;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

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
    private UserStatus status;
    private Set<Role> roles;
    private boolean mfaEnabled;
    private String mfaSecret;
    private Timestamp emailVerifiedAt;
    private int failedLoginCount;
    private Timestamp lockedUntil;
    private Timestamp lastLoginAt;
    private Timestamp passwordUpdatedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private UserProfile userProfile;
}
