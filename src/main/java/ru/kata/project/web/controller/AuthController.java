package ru.kata.project.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.project.core.dto.AuthenticationResponseCoreDto;
import ru.kata.project.core.usecase.AuthenticateUserUseCase;
import ru.kata.project.core.usecase.ForgotPasswordUseCase;
import ru.kata.project.core.usecase.LogoutUserUseCase;
import ru.kata.project.core.usecase.RefreshTokenUseCase;
import ru.kata.project.core.usecase.ResetPasswordUseCase;
import ru.kata.project.core.usecase.UserRegistrationUseCase;
import ru.kata.project.core.usecase.VerifyEmailUseCase;
import ru.kata.project.shared.utility.mapper.DtoMapper;
import ru.kata.project.web.dto.AuthenticationResponseDto;
import ru.kata.project.web.dto.EmailResetDto;
import ru.kata.project.web.dto.EmailVerificationCodeDto;
import ru.kata.project.web.dto.LoginRequestDto;
import ru.kata.project.web.dto.NewPasswordDto;
import ru.kata.project.web.dto.RegistrationRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRegistrationUseCase userRegistrationUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUserUseCase logoutUserUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequestDto registrationDto) {
        return ResponseEntity.ok(userRegistrationUseCase.execute(DtoMapper.toCore(registrationDto)));
    }

    @PostMapping("/verify/email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationCodeDto codeDto) {
        return ResponseEntity.ok(verifyEmailUseCase.execute(DtoMapper.toCore(codeDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseCoreDto> login(
            @RequestBody LoginRequestDto request,
            HttpServletResponse response) {
        return ResponseEntity.ok(authenticateUserUseCase.execute(DtoMapper.toCore(request), response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDto> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {
        return refreshTokenUseCase.execute(request, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(logoutUserUseCase.execute(request, response));
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailResetDto email) {
        return ResponseEntity.ok(forgotPasswordUseCase.execute(DtoMapper.toCore(email)));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody NewPasswordDto newPasswordDto) {
        return ResponseEntity.ok(resetPasswordUseCase.execute(DtoMapper.toCore(newPasswordDto)));
    }
}