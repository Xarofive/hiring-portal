package ru.kata.project.user.core.entity;

/**
 * UserStatus
 * <p>
 * Набор статусов для пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public enum UserStatus {
    ACTIVE,
    PENDING_EMAIL,
    LOCKED,
    DELETED
}
