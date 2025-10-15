package ru.kata.project.user.core.usecase;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.project.security.service.AuthAuditService;
import ru.kata.project.security.service.JwtService;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.port.Authenticator;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.dto.AuthenticationResponseDto;
import ru.kata.project.user.dto.LoginRequestDto;

import java.util.UUID;

/**
 * AuthenticateUserUseCase
 * <p>
 * Use-case для обработки сценария "Аутентификация пользователя".
 * </p>
 * <p>
 * Этот класс обрабатывает процесс входа пользователя в систему.
 * </p>
 * <ul>
 *  <li> проверка правильности логина и пароля через {@link Authenticator};</li>
 *  <li> загрузка данных пользователя через {@link UserRepository};</li>
 *  <li> генерация JWT access и refresh токенов через {@link JwtService};</li>
 *  <li> логирование события "AUTHENTICATE" через {@link AuthAuditService}.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final Authenticator authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthAuditService auditService;

    public AuthenticationResponseDto execute(LoginRequestDto request, HttpServletResponse response) {
        authenticationManager.authenticate(request.getUsername(), request.getPassword());
        final User user = userRepository.findByUsernameOrEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return generateTokens(user, response);
    }

    private AuthenticationResponseDto generateTokens(User user, HttpServletResponse response) {
        final String accessToken = jwtService.generateAccessToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);

        final UUID familyId = UUID.randomUUID();

        jwtService.saveUserToken(accessToken, refreshToken, user, familyId);
        jwtService.setRefreshCookie(refreshToken, response);

        auditService.logAudit(user.getId(), "AUTHENTICATE", "0.0.0.0:0000", "{json:json}");

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }
}
