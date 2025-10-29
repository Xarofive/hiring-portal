package ru.kata.project.user.core.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kata.project.user.core.dto.AuthenticationResponseDto;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.exception.InvalidTokenException;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.TokenRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.service.JwtService;

/**
 * RefreshTokenUseCase
 * <p>
 * Use-case для обработки сценария "Обновление токенов пользователя".
 * </p>
 * <ul>
 *  <li> поиск пользователя через {@link UserRepository};</li>
 *  <li> поиск токенов через {@link TokenRepository};</li>
 *  <li> обновление токенов через {@link JwtService};</li>
 *  <li> логирование события "REFRESH_TOKENS" через {@link AuthAuditService}.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenUseCase {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthAuditService auditService;

    public AuthenticationResponseDto execute(String refreshToken) {
        final User user = findUser(refreshToken);
        final Token oldToken = validateToken(refreshToken);

        if (!jwtService.isValidRefresh(refreshToken, user)) {
            jwtService.revokeFamily(oldToken.getFamilyId());
            throw new InvalidTokenException("Invalid refresh token");
        }

        return generateNewTokens(user, oldToken);
    }

    private User findUser(String refreshToken) {
        final String username;
        try {
            username = jwtService.extractUsername(refreshToken);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid refresh token format");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private Token validateToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));
    }

    private AuthenticationResponseDto generateNewTokens(User user, Token oldToken) {
        final String newAccessToken = jwtService.generateAccessToken(user);
        final String newRefreshToken = jwtService.generateRefreshToken(user);

        jwtService.revokeToken(oldToken);
        jwtService.saveUserToken(newAccessToken, newRefreshToken, user, oldToken.getFamilyId());

        auditService.logAudit(user.getId(), "REFRESH_TOKENS", "0.0.0.0:0000", "{json:json}");

        return new AuthenticationResponseDto(newAccessToken, newRefreshToken);
    }
}