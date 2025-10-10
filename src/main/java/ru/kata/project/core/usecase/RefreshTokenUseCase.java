package ru.kata.project.core.usecase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.project.core.entity.Token;
import ru.kata.project.core.entity.User;
import ru.kata.project.core.port.repository.TokenRepository;
import ru.kata.project.core.port.repository.UserRepository;
import ru.kata.project.shared.security.service.AuthAuditService;
import ru.kata.project.shared.security.service.JwtService;
import ru.kata.project.web.dto.AuthenticationResponseDto;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthAuditService auditService;

    public ResponseEntity<AuthenticationResponseDto> execute(HttpServletRequest request,
                                                             HttpServletResponse response) {

        String refreshToken = jwtService.extractRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username;
        try {
            username = jwtService.extractUsername(refreshToken);
        } catch (Exception e) {
            log.warn("Invalid refresh token format: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<Token> oldTokenOpt = tokenRepository.findByRefreshToken(refreshToken);
        if (oldTokenOpt.isEmpty()) {
            log.warn("Refresh token not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Token oldToken = oldTokenOpt.get();

        if (!jwtService.isValidRefresh(refreshToken, user)) {
            log.warn("Detected invalid refresh token — revoking family {}", oldToken.getFamilyId());
            jwtService.revokeFamily(oldToken.getFamilyId());
            jwtService.deleteRefreshCookie(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        jwtService.revokeToken(oldToken);
        jwtService.saveUserToken(newAccessToken, newRefreshToken, user, oldToken.getFamilyId());

        jwtService.setRefreshCookie(newRefreshToken, response);

        auditService.logAudit(user.getId(), "REFRESH_TOKENS", "0.0.0.0:0000", "{json:json}");

        return ResponseEntity.ok(new AuthenticationResponseDto(newAccessToken, newRefreshToken));
    }
}
