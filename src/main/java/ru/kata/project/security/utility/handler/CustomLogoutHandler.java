package ru.kata.project.security.utility.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import ru.kata.project.user.core.entity.Token;
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

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        final String token = authHeader.substring(7);

        final Token tokenEntity = tokenRepository.findByAccessToken(token).orElse(null);

        if (tokenEntity != null) {
            tokenEntity.setLoggedOut(true);
            tokenRepository.save(tokenEntity);
        }
    }
}
