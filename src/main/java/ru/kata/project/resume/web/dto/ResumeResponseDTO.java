package ru.kata.project.resume.web.dto;

import ru.kata.project.resume.core.entity.vo.Email;
import ru.kata.project.resume.core.entity.vo.Skill;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
/**
 * DTO для ответа с полной информацией о резюме
 */

public record ResumeResponseDTO(
        UUID id,
        UUID userId,
        String title,
        String summary,
        Email email,
        List<Skill> skills,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}