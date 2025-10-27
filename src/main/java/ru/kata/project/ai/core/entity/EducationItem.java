package ru.kata.project.ai.core.entity;

import java.time.Year;

public record EducationItem(String degree, String institution, Year start, Year end, String note) {
}
