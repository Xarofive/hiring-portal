package ru.kata.project.resume.utility.ResumeExeption;

public class ResumeValidationException extends RuntimeException {
    public ResumeValidationException(String message) {
        super(message);
    }
}