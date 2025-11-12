package ru.kata.project.resume.core.entity.vo;

import ru.kata.project.resume.utility.resumeExeption.ResumeValidationException;
/**
 * Skill
 * <p>
 *     VO для навыка с названием и уровнем
 * </p>
 */

public record Skill(String name, SkillLevel level)  {

    public Skill {
        if (name == null || name.isBlank()) {
            throw new ResumeValidationException("Название навыка не может быть пустым");
        }
        if (level == null) {
            throw new ResumeValidationException("Уровень навыка не может быть пустым");
        }
        name = name.trim().toLowerCase();
    }

    public enum SkillLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }
}