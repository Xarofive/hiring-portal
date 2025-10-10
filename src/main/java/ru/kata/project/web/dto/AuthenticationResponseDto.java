package ru.kata.project.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthenticationResponseDto {

    private String accessToken;

    private String refreshToken;

}