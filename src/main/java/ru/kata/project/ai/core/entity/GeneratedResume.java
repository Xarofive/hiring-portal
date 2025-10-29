package ru.kata.project.ai.core.entity;

import java.util.List;
import java.util.Locale;

/**
 * GeneratedResume
 * <p>
 * Результат генерации резюме.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record GeneratedResume(String title, String summary, List<String> skills, List<ExperienceItem> experience,
                              List<EducationItem> education, String templateVersion, Locale locale) {
}
