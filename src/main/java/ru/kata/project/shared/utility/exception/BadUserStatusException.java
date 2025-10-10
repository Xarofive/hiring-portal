package ru.kata.project.shared.utility.exception;

public class BadUserStatusException extends RuntimeException {

    public BadUserStatusException() {
        super("Статус пользователя запрещает вход.");
    }

    public BadUserStatusException(String message) {
        super(message);
    }

    public BadUserStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
