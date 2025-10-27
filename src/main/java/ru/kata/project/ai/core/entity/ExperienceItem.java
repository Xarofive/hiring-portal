package ru.kata.project.ai.core.entity;

import java.time.YearMonth;
import java.util.List;

public record ExperienceItem(String company, String role, YearMonth start, YearMonth end, String summary,
                             List<String> achievements) {
}
