package ru.kata.project.ai.adapters.rest.dto;

import ru.kata.project.ai.core.entity.EducationItem;
import ru.kata.project.ai.core.entity.ExperienceItem;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public record GeneratedResumeDto(String title, String summary, List<String> skills, List<ExperienceItem> experience,
                                 List<EducationItem> education, String templateVersion,
                                 Locale locale) implements Serializable {
}