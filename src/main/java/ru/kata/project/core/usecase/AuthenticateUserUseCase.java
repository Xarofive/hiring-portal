package ru.kata.project.core.usecase;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.project.core.dto.AuthenticationResponseCoreDto;
import ru.kata.project.core.dto.LoginRequestCoreDto;
import ru.kata.project.core.entity.User;
import ru.kata.project.core.port.Authenticator;
import ru.kata.project.core.port.repository.UserRepository;
import ru.kata.project.shared.security.service.AuthAuditService;
import ru.kata.project.shared.security.service.JwtService;
import ru.kata.project.shared.utility.enumeration.UserStatus;
import ru.kata.project.shared.utility.exception.BadUserStatusException;

import java.util.UUID;

@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final Authenticator authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthAuditService auditService;

    public AuthenticationResponseCoreDto execute(LoginRequestCoreDto request, HttpServletResponse response) {

        authenticationManager.authenticateWithUsername(request.getUsername(), request.getPassword());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getStatus() == null || user.getStatus().equals(UserStatus.PENDING_EMAIL)) {
            throw new BadUserStatusException("Пожалуйста, подтвердите почту.");
        }
        if (user.getStatus().equals(UserStatus.LOCKED)) {
            throw new BadUserStatusException("Пользователь заблокирован.");
        }
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        UUID familyId = UUID.randomUUID();

        jwtService.saveUserToken(accessToken, refreshToken, user, familyId);
        jwtService.setRefreshCookie(refreshToken, response);

        auditService.logAudit(user.getId(), "AUTHENTICATE", "0.0.0.0:0000", "{json:json}");

        return new AuthenticationResponseCoreDto(accessToken, refreshToken);
    }
}
