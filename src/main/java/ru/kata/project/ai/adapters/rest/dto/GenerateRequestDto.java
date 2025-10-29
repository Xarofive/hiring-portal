package ru.kata.project.ai.adapters.rest.dto;

import java.io.Serializable;
import java.util.List;

/**
 * GenerateRequestDto
 * <p>
 * DTO-класс, представляющий данные для генерации резюме для пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record GenerateRequestDto(String fullName, String targetRole, List<String> skills,
                                 List<ExperienceDto> experience, List<EducationDto> education,
                                 String locale) implements Serializable {
}