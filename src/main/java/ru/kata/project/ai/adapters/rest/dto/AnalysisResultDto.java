package ru.kata.project.ai.adapters.rest.dto;

import ru.kata.project.ai.core.entity.Recommendation;
import ru.kata.project.ai.core.entity.ResumeId;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * AnalysisResultDto
 * <p>
 * DTO-класс, представляющий результат анализа резюме пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record AnalysisResultDto(ResumeId resumeId, List<Recommendation> recommendations, Instant analyzedAt,
                                String analyzerVersion) implements Serializable {
}
