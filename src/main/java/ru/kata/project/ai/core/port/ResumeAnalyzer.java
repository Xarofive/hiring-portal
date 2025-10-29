package ru.kata.project.ai.core.port;

import ru.kata.project.ai.core.entity.AnalysisContext;
import ru.kata.project.ai.core.entity.AnalysisResult;
import ru.kata.project.ai.core.entity.ResumeId;

/**
 * ResumeAnalyzer
 * <p>
 * Порт для анализа резюме пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface ResumeAnalyzer {
    AnalysisResult analyze(ResumeId id, AnalysisContext context);
}
