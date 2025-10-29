package ru.kata.project.ai.core.entity;

import java.time.YearMonth;
import java.util.List;

/**
 * ExperienceItem
 * <p>
 * Модель описания одного места работы в резюме.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record ExperienceItem(String company, String role, YearMonth start, YearMonth end, String summary,
                             List<String> achievements) {
}
