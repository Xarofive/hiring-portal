package ru.kata.project.ai.application.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1553394163088578939L;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
