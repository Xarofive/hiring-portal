package ru.kata.project.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.kata.project.user.core.port.service.AuthenticationService;

/**
 * SpringAuthenticator
 * <p>
 * Реализация аутентификатора пользователей {@link AuthenticationService}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Component
public class SpringAuthenticationService implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public SpringAuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authenticate(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }
}
