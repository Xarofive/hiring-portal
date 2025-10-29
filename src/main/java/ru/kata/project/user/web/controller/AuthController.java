package ru.kata.project.user.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.project.user.core.dto.AuthenticationResponseDto;
import ru.kata.project.user.core.dto.EmailResetDto;
import ru.kata.project.user.core.dto.EmailVerificationCodeDto;
import ru.kata.project.user.core.dto.LoginRequestDto;
import ru.kata.project.user.core.dto.NewPasswordDto;
import ru.kata.project.user.core.dto.RegistrationRequestDto;
import ru.kata.project.user.core.port.service.JwtService;
import ru.kata.project.user.core.usecase.AuthenticateUserUseCase;
import ru.kata.project.user.core.usecase.ForgotPasswordUseCase;
import ru.kata.project.user.core.usecase.RefreshTokenUseCase;
import ru.kata.project.user.core.usecase.ResetPasswordUseCase;
import ru.kata.project.user.core.usecase.UserRegistrationUseCase;
import ru.kata.project.user.core.usecase.VerifyEmailUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRegistrationUseCase userRegistrationUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequestDto registrationDto) {
        return ResponseEntity.ok(userRegistrationUseCase.execute(registrationDto));
    }

    @PostMapping("/verify/email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationCodeDto codeDto) {
        return ResponseEntity.ok(verifyEmailUseCase.execute(codeDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(
            @RequestBody LoginRequestDto request,
            HttpServletResponse response) {
        final AuthenticationResponseDto tokens = authenticateUserUseCase.execute(request);

        jwtService.setRefreshCookie(tokens.refreshToken(), response);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDto> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {
        final String refreshToken = jwtService.extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final AuthenticationResponseDto tokens = refreshTokenUseCase.execute(refreshToken);
        jwtService.setRefreshCookie(tokens.refreshToken(), response);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailResetDto email) {
        return ResponseEntity.ok(forgotPasswordUseCase.execute(email));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody NewPasswordDto newPasswordDto) {
        return ResponseEntity.ok(resetPasswordUseCase.execute(newPasswordDto));
    }
}