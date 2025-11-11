package ru.kata.project.resume.web.dto;

import ru.kata.project.resume.core.entity.vo.Email;
import ru.kata.project.resume.core.entity.vo.Skill;
import ru.kata.project.resume.utility.ResumeExeption.ResumeValidationException;

import java.util.List;

/**
 * DTO запроса для работы с резюме.
 *
 * <p>Принимает данные от клиента, выполняет базовую валидацию и предоставляет их
 * в виде доменных объектов для дальнейшей обработки в бизнес-логике.</p>
 */

public record ResumeRequestDTO(
        String title,
        String summary,
        Email email,
        List<Skill> skills
) {
    public ResumeRequestDTO {
        if (title == null || title.trim().isBlank()) {
            throw new ResumeValidationException("Заголовок не может быть пустым");
        }
        if (title.length() > 100) {
            throw new ResumeValidationException("Заголовок очень длинный");
        }
        title.trim().toLowerCase();

        if (summary == null || summary.isBlank()) {
            throw new ResumeValidationException("Описание не может быть пустым");
        }
        if (summary.length() > 1000) {
            throw new ResumeValidationException("Описание очень длинное");
        }
        if (email == null) {
            throw new ResumeValidationException("Email не должен быть пустым");
        }
        if (skills == null || skills.isEmpty()) {
            throw new ResumeValidationException("Список навыков не может быть пустым");
        }
    }
}
