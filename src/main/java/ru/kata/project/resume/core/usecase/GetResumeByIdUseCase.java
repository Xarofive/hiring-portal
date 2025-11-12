package ru.kata.project.resume.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.utility.resumeExeption.ResumeNotFoundException;
import ru.kata.project.resume.utility.resumeExeption.ResumeValidationException;
import ru.kata.project.resume.web.dto.ResumeResponseDTO;
import ru.kata.project.resume.web.mapper.ResumeMapper;
import ru.kata.project.security.utility.SecurityUtils;

import java.util.UUID;
/**
 * Use Case для получения резюме по идентификатору.
 *
 * <p>Реализует бизнес-сценарий получения детальной информации о резюме по его ID.
 * Обеспечивает базовую аутентификацию и валидацию входных параметров.</p>
 *
 * <h3>Основной поток выполнения:</h3>
 * <ol>
 *   <li>Валидация входного параметра (resumeId не может быть null)</li>
 *   <li>Проверка аутентификации пользователя</li>
 *   <li>Поиск резюме в репозитории по ID</li>
 *   <li>Преобразование доменной сущности в DTO для возврата</li>
 * </ol>
 */

 @RequiredArgsConstructor
public class GetResumeByIdUseCase {

    private final ResumeRepository resumeRepository;

    public ResumeResponseDTO execute(UUID resumeId) {
        if (resumeId == null) {
            throw new ResumeValidationException("ID резюме не может быть пустым");
        }

        SecurityUtils.getAuthentication();

        final Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Резюме с ID " + resumeId + " не найдено"));

        return ResumeMapper.toResumeResponseDTO(resume);
    }
}