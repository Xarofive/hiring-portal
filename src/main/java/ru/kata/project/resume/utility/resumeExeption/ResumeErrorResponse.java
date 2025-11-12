package ru.kata.project.resume.utility.resumeExeption;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResumeErrorResponse {
    private final String errorCode;
    private final String message;
    private final LocalDateTime timestamp;

    public ResumeErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}