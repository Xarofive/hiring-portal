package ru.kata.project.ai.core.entity;

import java.util.Locale;

public record AnalysisContext(Locale locale, String targetRole, Seniority targetSeniority) {
}
