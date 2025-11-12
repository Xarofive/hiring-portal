package ru.kata.project.resume.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.utility.resumeExeption.ResumeNotFoundException;
import ru.kata.project.resume.utility.resumeExeption.ResumeValidationException;
import ru.kata.project.resume.web.dto.ResumeResponseDTO;
import ru.kata.project.resume.web.mapper.ResumeMapper;
import ru.kata.project.security.utility.SecurityUtils;

import java.util.List;
import java.util.UUID;
/**
 * Use Case для получения списка резюме пользователя.
 *
 * <p>Реализует бизнес-сценарий получения всех резюме, принадлежащих конкретному пользователю.
 * Обеспечивает строгую проверку прав доступа - только владелец или администратор могут
 * просматривать список резюме пользователя.</p>
 *
 * <h3>Бизнес-правила доступа:</h3>
 * <ul>
 *   <li>Владелец (пользователь с указанным userId) может просматривать свои резюме</li>
 *   <li>Администраторы (ROLE_ADMIN) могут просматривать резюме любого пользователя</li>
 *   <li>Другие пользователи не имеют доступа к списку резюме</li>
 * </ul>
 */

@RequiredArgsConstructor
public class GetResumesByUserUseCase {

    private final ResumeRepository resumeRepository;

    public List<ResumeResponseDTO> execute(UUID userId) {
        if (userId == null) {
            throw new ResumeValidationException("ID пользователя не может быть пустым");
        }

        final boolean isOwner = SecurityUtils.isCurrentUserOwner(userId);
        final boolean isAdmin = SecurityUtils.hasRole(SecurityUtils.ROLE_ADMIN);

        if (!isOwner && !isAdmin) {
            throw new SecurityException("Доступ запрещён: просматривать эти резюме могут только владелец или администратор");
        }

        final List<Resume> resumes = resumeRepository.findByUserId(userId);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("У пользователя нет резюме");
        }

        return ResumeMapper.toResumeResponseDTOList(resumes);
    }
}
