package ru.kata.project.core.usecase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kata.project.core.port.repository.TokenRepository;
import ru.kata.project.shared.security.service.AuthAuditService;
import ru.kata.project.shared.security.service.JwtService;

@Slf4j
@RequiredArgsConstructor
public class LogoutUserUseCase {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthAuditService auditService;

    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            log.debug("No refresh token in cookies");
            return "Недействительный токен";
        }

        tokenRepository.findByRefreshToken(refreshToken).ifPresent(token -> {
            jwtService.revokeFamily(token.getFamilyId());
            auditService.logAudit(token.getFamilyId(),
                    " : token family is revoke, user logout", "0.0.0.0:0000", "{json:json}");
        });

        jwtService.deleteRefreshCookie(response);

        return "Успешный выход из аккаунта";
    }

}
