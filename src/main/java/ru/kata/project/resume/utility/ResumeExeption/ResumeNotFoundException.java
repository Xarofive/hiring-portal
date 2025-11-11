package ru.kata.project.resume.utility.ResumeExeption;

public class ResumeNotFoundException extends RuntimeException {
    public ResumeNotFoundException(String message) {
        super(message);
    }
}