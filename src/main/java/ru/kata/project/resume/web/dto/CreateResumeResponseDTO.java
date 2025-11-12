package ru.kata.project.resume.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * DTO ответа после успешного создания резюме.
 */

public record CreateResumeResponseDTO(
        UUID id,
        LocalDateTime createdAt
) {
}