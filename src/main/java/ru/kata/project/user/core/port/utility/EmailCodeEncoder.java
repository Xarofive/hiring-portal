package ru.kata.project.user.core.port.utility;

/**
 * EmailCodeEncoder
 * <p>
 * Порт для кодирования верификационного email-кода пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface EmailCodeEncoder {
    String encode(String token);
}
