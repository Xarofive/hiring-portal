package ru.kata.project.core.port;

public interface Authenticator {
    void authenticateWithUsername(String username, String password);

    void authenticateWithEmail(String email, String password);
}
