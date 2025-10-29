package ru.kata.project.ai.adapters.rest.dto;

import java.io.Serializable;

/**
 * ExperienceDto
 * <p>
 * DTO-класс, представляющий данные об опыте работы пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record ExperienceDto(String company, String role, String start, String end,
                            String summary) implements Serializable {
}