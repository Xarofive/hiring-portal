package ru.kata.project.ai.core.entity;

import java.util.Locale;

/**
 * AnalysisContext
 * <p>
 * Контекст анализа резюме.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record AnalysisContext(Locale locale, String targetRole, Seniority targetSeniority) {
}
