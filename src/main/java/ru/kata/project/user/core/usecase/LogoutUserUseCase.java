package ru.kata.project.user.core.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kata.project.user.core.port.repository.TokenRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.service.JwtService;

/**
 * LogoutUserUseCase
 * <p>
 * Use-case для обработки сценария "Выход пользователя из системы".
 * </p>
 * <ul>
 *  <li> поиск токенов через {@link TokenRepository};</li>
 *  <li> обнуление действующей семьи токенов через {@link JwtService};</li>
 *  <li> логирование события "LOGOUT" через {@link AuthAuditService}.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@Slf4j
@RequiredArgsConstructor
public class LogoutUserUseCase {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthAuditService auditService;

    public void execute(String refreshToken) {
        if (refreshToken == null) {
            return;
        }

        tokenRepository.findByRefreshToken(refreshToken).ifPresent(token -> {
            jwtService.revokeFamily(token.getFamilyId());
            auditService.logAudit(token.getUserId(), "LOGOUT", "0.0.0.0:0000", "{json:json}");
        });
    }
}
