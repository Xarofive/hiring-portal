package ru.kata.project.resume.utility.resumeExeption;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ResumeDataException extends RuntimeException {
    private final UUID resumeId;
    private final UUID userId;

    public ResumeDataException(String message, UUID resumeId, UUID userId, Throwable cause) {
        super(message, cause);
        this.resumeId = resumeId;
        this.userId = userId;
    }

    public ResumeDataException(String message, UUID userId, Throwable cause) {
        this(message, null, userId, cause);
    }

    public ResumeDataException(String message, Throwable cause, UUID resumeId) {
        this(message, resumeId, null, cause);
    }
}