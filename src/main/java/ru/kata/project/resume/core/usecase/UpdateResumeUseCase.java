package ru.kata.project.resume.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.utility.resumeExeption.ResumeNotFoundException;
import ru.kata.project.resume.utility.resumeExeption.ResumeValidationException;
import ru.kata.project.resume.web.dto.ResumeRequestDTO;
import ru.kata.project.resume.web.dto.ResumeResponseDTO;
import ru.kata.project.security.utility.SecurityUtils;

import java.util.UUID;
/**
 * Use Case для обновления существующего резюме.
 *
 * <p>Реализует бизнес-сценарий изменения данных резюме с полной проверкой прав доступа
 * и валидацией. Обеспечивает атомарное обновление полей резюме с автоматическим
 * обновлением временной метки modifiedAt.</p>
 *
 * <h3>Бизнес-правила обновления:</h3>
 * <ul>
 *   <li>Обновлять резюме может только его владелец (пользователь, создавший резюме)</li>
 *   <li>Все изменения должны проходить валидацию на уровне доменной сущности</li>
 *   <li>Временная метка updatedAt обновляется автоматически при изменении</li>
 * </ul>
 */

@RequiredArgsConstructor
public class UpdateResumeUseCase {

    private final ResumeRepository resumeRepository;

    public ResumeResponseDTO execute(UUID resumeId, ResumeRequestDTO request) {
        if (resumeId == null || request == null) {
            throw new ResumeValidationException("ID резюме и запрос на обновление не могут быть пустыми");
        }

        final Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Резюме с ID " + resumeId + " не найдено для обновления"));

        SecurityUtils.checkOwnership(resume.getUserId());
        resume.update(request.getTitle(), request.getSummary(), request.getEmail(), request.getSkills());
        final Resume updatedResume = resumeRepository.save(resume);

        return new ResumeResponseDTO(
                updatedResume.getId(),
                updatedResume.getUserId(),
                updatedResume.getTitle(),
                updatedResume.getSummary(),
                updatedResume.getEmail(),
                updatedResume.getSkillList(),
                updatedResume.getCreatedAt(),
                updatedResume.getUpdatedAt()
        );
    }
}