package ru.kata.project.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthenticationResponseCoreDto {

    private String accessToken;

    private String refreshToken;

}