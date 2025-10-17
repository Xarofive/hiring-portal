package ru.kata.project.user.core.port;

/**
 * Authenticator
 * <p>
 * Порт для аутентификации пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface Authenticator {

    void authenticate(String username, String password);
}
