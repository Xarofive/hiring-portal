package ru.kata.project.ai.application.port;

import java.util.UUID;

/**
 * ResumeRepository
 * <p>
 * Порт, представляющий репозиторий для хранения резюме пользователей.
 * </p>
 * <p>
 * Планируется вместо этого интерфейса использовать реальный репозиторий из модуля "Resume" (не реализован).
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface ResumeRepository {
    boolean existsById(UUID id);
}
