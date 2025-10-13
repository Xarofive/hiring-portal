package ru.kata.project.user.core.port;

/**
 * EmailCodeEncoder
 * <p>
 * Порт для кодирования пароля пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface UserPasswordEncoder {
    String encode(String rawPassword);
}
