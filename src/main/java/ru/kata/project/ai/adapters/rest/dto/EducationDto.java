package ru.kata.project.ai.adapters.rest.dto;

import java.io.Serializable;

/**
 * EducationDto
 * <p>
 * DTO-класс, представляющий данные об обучении пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record EducationDto(String degree, String institution, Integer start, Integer end,
                           String note) implements Serializable {
}