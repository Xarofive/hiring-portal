package ru.kata.project.security.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.kata.project.security.entity.UserDetailsEntity;
import ru.kata.project.user.core.exception.UserNotFoundException;

import java.util.UUID;

/**
 * Класс для проверки пользователя.
 *
 * <p>Предоставляет статические методы для получения информации о текущем аутентифицированном пользователе,
 * проверки ролей и прав доступа к ресурсам.</p>
 *
 * <ul>
 *   <li>Получение данных аутентификации текущего пользователя</li>
 *   <li>Извлечение идентификатора пользователя из контекста безопасности</li>
 *   <li>Проверка наличия ролей у пользователя</li>
 *   <li>Валидация прав доступа к ресурсам</li>
 *   <li>Проверка владения ресурсом</li>
 * </ul>
 */

public class SecurityUtils {

    public static final String ROLE_CANDIDATE = "ROLE_CANDIDATE";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static Authentication getAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            throw new UserNotFoundException("Пользователь не аутентифицирован");
        }
        return authentication;
    }

    public static UUID getCurrentUserId() {
        final Authentication authentication = getAuthentication();
        final Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsEntity) {
            return ((UserDetailsEntity) principal).getUser().getId();
        }
        throw new UserNotFoundException("Не удается извлечь id пользователя из данных аутентификации");
    }

    public static boolean hasRole(String role) {
        final Authentication authentication = getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(role));
    }

    public static void checkRole(String requiredRole) {
        if (!hasRole(requiredRole)) {
            throw new UserNotFoundException("Доступ запрещён: у пользователя нет необходимой роли: " + requiredRole);
        }
    }

    public static void checkOwnership(UUID resourceOwnerId) {
        final UUID currentUserId = getCurrentUserId();
        if (!currentUserId.equals(resourceOwnerId)) {
            throw new UserNotFoundException("Доступ запрещён: пользователь не является владельцем");
        }
    }

    public static boolean isCurrentUserOwner(UUID resourceOwnerId) {
        try {
            final UUID currentUserId = getCurrentUserId();
            return currentUserId.equals(resourceOwnerId);
        } catch (UserNotFoundException e) {
            return false;
        }
    }
}
