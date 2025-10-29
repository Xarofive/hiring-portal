package ru.kata.project.ai.core.entity;

import java.time.Year;

/**
 * EducationItem
 * <p>
 * Модель описания образования пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record EducationItem(String degree, String institution, Year start, Year end, String note) {
}
