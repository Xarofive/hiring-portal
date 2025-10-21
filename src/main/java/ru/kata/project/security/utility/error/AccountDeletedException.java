package ru.kata.project.security.utility.error;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

public class AccountDeletedException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = -3873001914708084636L;

    public AccountDeletedException(String msg) {
        super(msg);
    }
}