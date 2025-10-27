package ru.kata.project.ai.core.entity;

import java.time.Instant;
import java.util.List;

public record AnalysisResult(ResumeId resumeId, List<Recommendation> recommendations, Instant analyzedAt,
                             String analyzerVersion) {
}
