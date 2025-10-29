package ru.kata.project.ai.adapters.stub;

import ru.kata.project.ai.core.entity.EducationItem;
import ru.kata.project.ai.core.entity.ExperienceItem;
import ru.kata.project.ai.core.entity.GeneratedResume;
import ru.kata.project.ai.core.entity.GenerationContext;
import ru.kata.project.ai.core.port.ResumeGenerator;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * StubResumeGenerator
 * <p>
 * Класс-заглушка, представляющий генератор резюме для пользователя {@link ResumeGenerator}.
 * </p>
 * <p>
 * Создаёт сгенерированное резюме по данным, присланным пользователем.
 * Переводится по локали.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public class StubResumeGenerator implements ResumeGenerator {

    private static final String TEMPLATE_VERSION = "stub-1.0.0";

    @Override
    public GeneratedResume generate(GenerationContext context) {

        String fullName = context.fullName();
        if (fullName == null || fullName.isBlank()) {
            fullName = "Anonymous Candidate";
        }

        String role = context.targetRole();
        if (role == null || role.isBlank()) {
            role = "Specialist";
        }

        List<String> skills = context.skills();
        if (skills == null || skills.isEmpty()) {
            skills = List.of("Communication", "Teamwork");
        }

        List<ExperienceItem> experience = context.experience();
        if (experience == null) {
            experience = List.of();
        }

        List<EducationItem> education = context.education();
        if (education == null) {
            education = List.of();
        }

        final Locale locale = context.locale();
        final String title = role + " — " + fullName;
        final String summary = buildSummary(role, fullName, locale);

        return new GeneratedResume(
                title,
                summary,
                skills,
                experience,
                education,
                TEMPLATE_VERSION,
                locale
        );
    }

    private String buildSummary(String role, String fullName, Locale locale) {
        if (Objects.equals(locale.getLanguage(), "ru")) {
            return "Черновик резюме для роли " + role + " кандидата " + fullName + ".";
        }
        return "Draft resume for the role of " + role + " (" + fullName + ").";
    }
}