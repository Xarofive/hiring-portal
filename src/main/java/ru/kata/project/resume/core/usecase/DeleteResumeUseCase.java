package ru.kata.project.resume.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.utility.resumeExeption.ResumeNotFoundException;
import ru.kata.project.resume.utility.resumeExeption.ResumeValidationException;
import ru.kata.project.security.utility.SecurityUtils;

import java.util.UUID;
/**
 * Use Case для удаления резюме.
 *
 * <p>Реализует бизнес-сценарий удаления резюме с проверкой прав доступа и валидацией.
 * Обеспечивает безопасное удаление данных с соблюдением правил авторизации.</p>
 *
 * <h3>Бизнес-правила удаления:</h3>
 * <ul>
 *   <li>Удалять резюме может только его владелец (пользователь, создавший резюме)</li>
 *   <li>Администраторы (ROLE_ADMIN) могут удалять любые резюме</li>
 *   <li>Проверяется существование резюме перед удалением</li>
 * </ul>
 */

@RequiredArgsConstructor
public class DeleteResumeUseCase {

    private final ResumeRepository resumeRepository;

    public void execute(UUID resumeId) {
        if (resumeId == null) {
            throw new ResumeValidationException("ID удаляемого резюме не может быть null");
        }

        final Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Резюме с ID " + resumeId + " не найдено для удаления"));

        final boolean isOwner = SecurityUtils.isCurrentUserOwner(resume.getUserId());
        final boolean isAdmin = SecurityUtils.hasRole(SecurityUtils.ROLE_ADMIN);

        if (!isOwner && !isAdmin) {
            throw new SecurityException("Доступ запрещён: удалять резюме могут только владелец или администратор");
        }

        final boolean deleted = resumeRepository.deleteById(resumeId);
        if (!deleted) {
            throw new ResumeNotFoundException("Не удалось удалить резюме с ID " + resumeId);
        }
    }
}
