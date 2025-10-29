package ru.kata.project.user.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.user.core.dto.AuthenticationResponseDto;
import ru.kata.project.user.core.dto.LoginRequestDto;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.service.AuthenticationService;
import ru.kata.project.user.core.port.service.JwtService;

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
 *  <li> проверка правильности логина и пароля через {@link AuthenticationService};</li>
 *  <li> загрузка данных пользователя через {@link UserRepository};</li>
 *  <li> генерация JWT access и refresh токенов через {@link JwtService};</li>
 *  <li> логирование события "AUTHENTICATE" через {@link AuthAuditService}.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthAuditService auditService;

    public AuthenticationResponseDto execute(LoginRequestDto request) {
        authenticationService.authenticate(request.username(), request.password());
        final User user = userRepository.findByUsernameOrEmail(request.username())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return generateTokens(user);
    }

    private AuthenticationResponseDto generateTokens(User user) {
        final String accessToken = jwtService.generateAccessToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);

        final UUID familyId = UUID.randomUUID();

        jwtService.saveUserToken(accessToken, refreshToken, user, familyId);

        auditService.logAudit(user.getId(), "AUTHENTICATE", "0.0.0.0:0000", "{json:json}");

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }
}
