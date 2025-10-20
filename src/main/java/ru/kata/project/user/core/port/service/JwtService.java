package ru.kata.project.user.core.port.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.entity.User;

import java.util.Date;
import java.util.UUID;

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
