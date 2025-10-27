package ru.kata.project.ai.adapters.rest.dto;

import ru.kata.project.ai.core.entity.Recommendation;
import ru.kata.project.ai.core.entity.ResumeId;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record AnalysisResultDto(ResumeId resumeId, List<Recommendation> recommendations, Instant analyzedAt,
                                String analyzerVersion) implements Serializable {
}
