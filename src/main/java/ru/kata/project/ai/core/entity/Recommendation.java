package ru.kata.project.ai.core.entity;

import java.util.Map;

public record Recommendation(String code, Severity severity, String message, Section section,
                             Map<String, Object> meta) {
}
