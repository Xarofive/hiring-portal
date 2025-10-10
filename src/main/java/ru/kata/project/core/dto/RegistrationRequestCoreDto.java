package ru.kata.project.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class RegistrationRequestCoreDto {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;
}