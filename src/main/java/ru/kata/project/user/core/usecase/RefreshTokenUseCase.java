package ru.kata.project.user.core.usecase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.port.repository.TokenRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.shared.dto.AuthenticationResponseDto;
import ru.kata.project.user.shared.security.service.AuthAuditService;
import ru.kata.project.user.shared.security.service.JwtService;

/**
 * RefreshTokenUseCase
 * <p>
 * Use-case, отвечающий за обновление действующих токенов пользователя.
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

    public ResponseEntity<AuthenticationResponseDto> execute(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractToken(request);
        User user = findUser(refreshToken);
        Token oldToken = validateToken(refreshToken);

        if (!isTokenValid(refreshToken, user)) {
            revokeInvalidTokenFamily(oldToken, response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return generateNewTokens(user, oldToken, response);
    }

    private String extractToken(HttpServletRequest request) {
        String refreshToken = jwtService.extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing refresh token");
        }
        return refreshToken;
    }

    private User findUser(String refreshToken) {
        try {
            return userRepository.findByUsername(jwtService.extractUsername(refreshToken))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        } catch (Exception e) {
            log.warn("Invalid refresh token format: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }

    private Token validateToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token not found"));
    }

    private boolean isTokenValid(String refreshToken, User user) {
        return jwtService.isValidRefresh(refreshToken, user);
    }

    private void revokeInvalidTokenFamily(Token oldToken, HttpServletResponse response) {
        log.warn("Detected invalid refresh token — revoking family {}", oldToken.getFamilyId());
        jwtService.revokeFamily(oldToken.getFamilyId());
        jwtService.deleteRefreshCookie(response);
    }

    private ResponseEntity<AuthenticationResponseDto> generateNewTokens(User user, Token oldToken, HttpServletResponse response) {
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        jwtService.revokeToken(oldToken);
        jwtService.saveUserToken(newAccessToken, newRefreshToken, user, oldToken.getFamilyId());
        jwtService.setRefreshCookie(newRefreshToken, response);

        auditService.logAudit(user.getId(), "REFRESH_TOKENS", "0.0.0.0:0000", "{json:json}");

        return ResponseEntity.ok(new AuthenticationResponseDto(newAccessToken, newRefreshToken));
    }
}
