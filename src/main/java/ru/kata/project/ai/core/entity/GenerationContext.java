package ru.kata.project.ai.core.entity;

import java.util.List;
import java.util.Locale;

public record GenerationContext(String fullName, String targetRole, List<String> skills,
                                List<ExperienceItem> experience, List<EducationItem> education, Locale locale) {
}