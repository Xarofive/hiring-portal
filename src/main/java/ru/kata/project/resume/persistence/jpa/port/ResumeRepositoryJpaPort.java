package ru.kata.project.resume.persistence.jpa.port;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.project.resume.persistence.jpa.entity.ResumeEntity;

import java.util.List;
import java.util.UUID;

public interface ResumeRepositoryJpaPort extends JpaRepository<ResumeEntity, UUID> {
    List<ResumeEntity> findByUserId(UUID userId);

    boolean existsByUserIdAndTitle(UUID userId, String title);
}
