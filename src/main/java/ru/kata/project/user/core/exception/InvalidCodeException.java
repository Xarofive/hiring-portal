package ru.kata.project.user.core.exception;

import java.io.Serial;

public class InvalidCodeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5375222189209410922L;

    public InvalidCodeException() {
        super("Invalid code format");
    }

    public InvalidCodeException(String message) {
        super(message);
    }
}
