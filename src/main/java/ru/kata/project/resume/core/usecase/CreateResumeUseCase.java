package ru.kata.project.resume.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.utility.ResumeExeption.ResumeValidationException;
import ru.kata.project.resume.web.dto.CreateResumeResponseDTO;
import ru.kata.project.resume.web.dto.ResumeRequestDTO;
import ru.kata.project.resume.web.mapper.ResumeMapper;
import ru.kata.project.security.utility.SecurityUtils;

import java.util.UUID;
/**
 * Use Case для создания нового резюме.
 *
 * <p>Реализует бизнес-сценарий создания резюме, включая:
 * <ul>
 *   <li>Валидацию входных данных</li>
 *   <li>Проверку прав доступа (только для кандидатов)</li>
 *   <li>Создание доменной сущности Resume</li>
 *   <li>Сохранение через репозиторий</li>
 *   <li>Преобразование результата в DTO</li>
 * </ul>
 * </p>
 */

@RequiredArgsConstructor
public class CreateResumeUseCase {

    private final ResumeRepository resumeRepository;

    public CreateResumeResponseDTO execute(ResumeRequestDTO request) {
        if (request == null) {
            throw new ResumeValidationException("Запрос на создание резюме не может быть пустым");
        }

        SecurityUtils.checkRole(SecurityUtils.ROLE_CANDIDATE);

        final UUID currentUserId = SecurityUtils.getCurrentUserId();

        final Resume resume = new Resume(
                currentUserId,
                request.title(),
                request.summary(),
                request.email(),
                request.skills()
        );

        final Resume savedResume = resumeRepository.save(resume);

        return ResumeMapper.toCreateResumeResponseDTO(savedResume);
    }
}