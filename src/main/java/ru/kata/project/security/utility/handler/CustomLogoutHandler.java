package ru.kata.project.security.utility.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import ru.kata.project.user.core.port.service.JwtService;
import ru.kata.project.user.core.usecase.LogoutUserUseCase;
import ru.kata.project.user.persistence.repository.jpa.TokenRepositoryJpa;

/**
 * CustomLogoutHandler
 * <p>
 * Обработчик выхода пользователя из системы.
 * </p>
 * <p>
 * Используется для установки флага loggedOut для токена после выхода пользователя из системы.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepositoryJpa tokenRepository;
    private final JwtService jwtService;
    private final LogoutUserUseCase logoutUserUseCase;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String accessToken = authHeader.substring(7);
            tokenRepository.findByAccessToken(accessToken).ifPresent(token -> {
                token.setLoggedOut(true);
                tokenRepository.save(token);
            });
        }

        final String refreshToken = jwtService.extractRefreshTokenFromCookie(request);
        logoutUserUseCase.execute(refreshToken);

        jwtService.deleteRefreshCookie(response);
    }
}
