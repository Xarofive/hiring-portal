package ru.kata.project.resume.utility.resumeExeption;

public class ResumeValidationException extends RuntimeException {
    public ResumeValidationException(String message) {
        super(message);
    }
}