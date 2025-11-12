package ru.kata.project.resume.core.port;

import ru.kata.project.resume.core.entity.Resume;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * ResumeRepository
 * <p>
 *     Порт для взаимодействия с Resume
 * </p>
 */

public interface ResumeRepository {
    Resume save(Resume resume);

    boolean deleteById(UUID id);

    Optional<Resume> findById(UUID resumeId);

    List<Resume> findByUserId(UUID userId);
}