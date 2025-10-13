package ru.kata.project.user.shared.utility.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * CustomAccessDeniedHandler
 * <p>
 * Обработчик ошибки {@link AccessDeniedException}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        response.setStatus(403);
    }
}
