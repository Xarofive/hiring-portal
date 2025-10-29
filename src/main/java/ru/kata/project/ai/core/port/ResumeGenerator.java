package ru.kata.project.ai.core.port;

import ru.kata.project.ai.core.entity.GeneratedResume;
import ru.kata.project.ai.core.entity.GenerationContext;

/**
 * ResumeGenerator
 * <p>
 * Порт для генерации резюме пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface ResumeGenerator {
    GeneratedResume generate(GenerationContext context);
}
