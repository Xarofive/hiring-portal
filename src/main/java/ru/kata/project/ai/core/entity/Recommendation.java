package ru.kata.project.ai.core.entity;

import java.util.Map;

/**
 * Recommendation
 * <p>
 * Рекомендация, полученная в результате анализа резюме.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record Recommendation(String code, Severity severity, String message, Section section,
                             Map<String, Object> meta) {
}
