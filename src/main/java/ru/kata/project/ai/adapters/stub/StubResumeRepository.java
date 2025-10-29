package ru.kata.project.ai.adapters.stub;

import ru.kata.project.ai.application.port.ResumeRepository;

import java.util.UUID;

/**
 * StubResumeRepository
 * <p>
 * Класс-заглушка, представляющий репозиторий для хранения резюме пользователей {@link ResumeRepository}.
 * </p>
 * <p>
 * Возвращает true, если id неравен - 00000000-0000-0000-0000-000000000000.
 * </p>
 * <p>
 * Планируется вместо этой заглушки использовать реальный класс репозитория из модуля "Resume" (не реализован).
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public class StubResumeRepository implements ResumeRepository {
    @Override
    public boolean existsById(UUID id) {
        return !id.equals(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }
}
