package ru.kata.project.user.core.port;

/**
 * EmailCodeGenerator
 * <p>
 * Порт для генерирования верификационного email-кода пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface EmailCodeGenerator {
    String generate();
}
