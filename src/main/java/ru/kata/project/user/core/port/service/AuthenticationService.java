package ru.kata.project.user.core.port.service;

/**
 * Authenticator
 * <p>
 * Порт для аутентификации пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface AuthenticationService {

    void authenticate(String username, String password);
}
