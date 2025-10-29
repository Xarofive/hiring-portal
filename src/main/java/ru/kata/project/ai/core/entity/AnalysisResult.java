package ru.kata.project.ai.core.entity;

import java.time.Instant;
import java.util.List;

/**
 * AnalysisResult
 * <p>
 * Результат анализа резюме, выполненного AI-анализатором.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record AnalysisResult(ResumeId resumeId, List<Recommendation> recommendations, Instant analyzedAt,
                             String analyzerVersion) {
}
