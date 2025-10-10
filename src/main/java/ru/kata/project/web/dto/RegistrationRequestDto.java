package ru.kata.project.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class RegistrationRequestDto {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;
}