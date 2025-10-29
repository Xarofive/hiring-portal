package ru.kata.project.ai.adapters.rest.dto;

import ru.kata.project.ai.core.entity.Seniority;

import java.io.Serializable;

/**
 * AnalyzeRequestDto
 * <p>
 * DTO-класс, представляющий запрос пользователя на анализ своего резюме по заданным параметрам.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record AnalyzeRequestDto(String locale, String targetRole, Seniority targetSeniority) implements Serializable {
}