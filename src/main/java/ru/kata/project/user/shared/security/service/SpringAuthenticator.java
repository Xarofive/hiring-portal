package ru.kata.project.user.shared.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.kata.project.user.core.port.Authenticator;

/**
 * SpringAuthenticator
 * <p>
 * Реализация аутентификатора пользователей {@link Authenticator}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Component
public class SpringAuthenticator implements Authenticator {

    private final AuthenticationManager authenticationManager;

    public SpringAuthenticator(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authenticate(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

}
