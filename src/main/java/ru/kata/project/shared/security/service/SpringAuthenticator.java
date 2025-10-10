package ru.kata.project.shared.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.kata.project.core.port.Authenticator;

@Component
public class SpringAuthenticator implements Authenticator {

    private final AuthenticationManager authenticationManager;

    public SpringAuthenticator(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authenticateWithUsername(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    @Override
    public void authenticateWithEmail(String email, String password) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(email, password)
//        );
    }
}
