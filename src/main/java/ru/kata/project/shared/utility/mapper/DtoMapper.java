package ru.kata.project.shared.utility.mapper;

import ru.kata.project.core.dto.AuthenticationResponseCoreDto;
import ru.kata.project.core.dto.EmailResetCoreDto;
import ru.kata.project.core.dto.EmailVerificationCodeCoreDto;
import ru.kata.project.core.dto.LoginRequestCoreDto;
import ru.kata.project.core.dto.NewPasswordCoreDto;
import ru.kata.project.core.dto.RegistrationRequestCoreDto;
import ru.kata.project.web.dto.AuthenticationResponseDto;
import ru.kata.project.web.dto.EmailResetDto;
import ru.kata.project.web.dto.EmailVerificationCodeDto;
import ru.kata.project.web.dto.LoginRequestDto;
import ru.kata.project.web.dto.NewPasswordDto;
import ru.kata.project.web.dto.RegistrationRequestDto;

public class DtoMapper {

    public static RegistrationRequestCoreDto toCore(RegistrationRequestDto webDto) {
        return webDto == null ? null : RegistrationRequestCoreDto.builder()
                .username(webDto.getUsername())
                .email(webDto.getEmail())
                .password(webDto.getPassword())
                .firstName(webDto.getFirstName())
                .lastName(webDto.getLastName())
                .build();
    }

    public static RegistrationRequestDto toWeb(RegistrationRequestCoreDto coreDto) {
        return coreDto == null ? null : RegistrationRequestDto.builder()
                .username(coreDto.getUsername())
                .email(coreDto.getEmail())
                .password(coreDto.getPassword())
                .firstName(coreDto.getFirstName())
                .lastName(coreDto.getLastName())
                .build();
    }

    public static LoginRequestCoreDto toCore(LoginRequestDto webDto) {
        return webDto == null ? null : LoginRequestCoreDto.builder()
                .username(webDto.getUsername())
                .email(webDto.getEmail())
                .password(webDto.getPassword())
                .build();
    }

    public static LoginRequestDto toWeb(LoginRequestCoreDto coreDto) {
        return coreDto == null ? null : LoginRequestDto.builder()
                .username(coreDto.getUsername())
                .email(coreDto.getEmail())
                .password(coreDto.getPassword())
                .build();
    }

    public static AuthenticationResponseCoreDto toCore(AuthenticationResponseDto webDto) {
        return webDto == null ? null : AuthenticationResponseCoreDto.builder()
                .accessToken(webDto.getAccessToken())
                .refreshToken(webDto.getRefreshToken())
                .build();
    }

    public static AuthenticationResponseDto toWeb(AuthenticationResponseCoreDto coreDto) {
        return coreDto == null ? null : AuthenticationResponseDto.builder()
                .accessToken(coreDto.getAccessToken())
                .refreshToken(coreDto.getRefreshToken())
                .build();
    }

    public static EmailResetCoreDto toCore(EmailResetDto webDto) {
        return webDto == null ? null : EmailResetCoreDto.builder()
                .email(webDto.getEmail())
                .build();
    }

    public static EmailResetDto toWeb(EmailResetCoreDto coreDto) {
        return coreDto == null ? null : EmailResetDto.builder()
                .email(coreDto.getEmail())
                .build();
    }

    public static EmailVerificationCodeCoreDto toCore(EmailVerificationCodeDto webDto) {
        return webDto == null ? null : EmailVerificationCodeCoreDto.builder()
                .code(webDto.getCode())
                .build();
    }

    public static EmailVerificationCodeDto toWeb(EmailVerificationCodeCoreDto coreDto) {
        return coreDto == null ? null : EmailVerificationCodeDto.builder()
                .code(coreDto.getCode())
                .build();
    }

    public static NewPasswordCoreDto toCore(NewPasswordDto webDto) {
        return webDto == null ? null : NewPasswordCoreDto.builder()
                .code(webDto.getCode())
                .password(webDto.getPassword())
                .build();
    }

    public static NewPasswordDto toWeb(NewPasswordCoreDto coreDto) {
        return coreDto == null ? null : NewPasswordDto.builder()
                .code(coreDto.getCode())
                .password(coreDto.getPassword())
                .build();
    }
}