package ru.kata.project.user.core.exception;

import java.io.Serial;

public class InvalidTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 9031963623171987690L;

    public InvalidTokenException() {
        super("Invalid token format");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}