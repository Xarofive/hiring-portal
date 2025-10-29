package ru.kata.project.ai.core.entity;

import java.util.List;
import java.util.Locale;

/**
 * GenerationContext
 * <p>
 * Контекст генерации резюме — исходные данные, на основе которых создаётся новый шаблон резюме.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record GenerationContext(String fullName, String targetRole, List<String> skills,
                                List<ExperienceItem> experience, List<EducationItem> education, Locale locale) {
}