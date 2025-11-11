package ru.kata.project.resume.core.entity.vo;

import ru.kata.project.resume.utility.ResumeExeption.ResumeValidationException;

import java.util.regex.Pattern;

/**
 * Email
 * <p>
 * VO для email с валидацией
 * Неизменяемый объект, проверяющий корректность email при создании
 * </p>
 */
public record Email(String value) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new ResumeValidationException("Email не может быть пустым");
        }

        final String normalizedEmail = value.toLowerCase().trim();

        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new ResumeValidationException("Неверный формат email: " + value);
        }

        value = normalizedEmail;
    }

    @Override
    public String toString() {
        return value;
    }
}