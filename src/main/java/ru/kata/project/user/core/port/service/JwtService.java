package ru.kata.project.user.core.port.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.entity.User;

import java.util.Date;
import java.util.UUID;

/**
 * JwtService
 * <p>
 * Порт для работы с JWT-токенами.
 * Определяет контракты для генерации, валидации, хранения и аннулирования токенов.
 * </p>
 * <ul>
 *  <li>Создание access и refresh токенов для пользователя;</li>
 *  <li>Проверка токенов на валидность и срок действия;</li>
 *  <li>Извлечение информации из токена (username, jti, kid, expiration);</li>
 *  <li>Сохранение и отзыв токенов в хранилище;</li>
 *  <li>Работа с refresh-токенами через HTTP cookies.</li>
 * </ul>
 * <p>
 * Реализация интерфейса должна соответствовать требованиям безопасности, установленным для JWT-токенов.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    boolean isValid(String token, UserDetails user);

    boolean isValidRefresh(String token, User user);

    boolean isExpired(String token);

    Date extractExpiration(String token);

    String extractUsername(String token);

    String extractJti(String token);

    String extractKidFromHeader(String token);

    void saveUserToken(String accessToken, String refreshToken, User user, UUID familyId);

    void revokeToken(Token token);

    void revokeFamily(UUID familyId);

    void setRefreshCookie(String refreshToken, HttpServletResponse response);

    void deleteRefreshCookie(HttpServletResponse response);

    String extractRefreshTokenFromCookie(HttpServletRequest request);
}
