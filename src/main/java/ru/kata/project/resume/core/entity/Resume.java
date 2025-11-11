package ru.kata.project.resume.core.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.kata.project.resume.core.entity.vo.Email;
import ru.kata.project.resume.core.entity.vo.Skill;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Resume
 * <p>
 * Представляет сущность Resume. Содержит основную информацию о профессиональном профиле пользователя,
 * включая контактные данные, навыки и опыт. </p>
 *
 * <p>Использует Value Objects (Email, Skill) для типизации данных
 * и обеспечения валидации на уровне доменной модели.</p>
 */
@Builder
@Getter
public class Resume {
    private UUID id;
    private final UUID userId;
    @Setter
    private String title;
    @Setter
    private String summary;
    @Setter
    private Email email;
    @Setter
    private List<Skill> skillList;
    private final LocalDateTime createdAt;
    @Setter
    private LocalDateTime updatedAt;

    public Resume(UUID userId, String title, String summary, Email email, List<Skill> skillList) {
        this.userId = userId;
        this.title = title;
        this.summary = summary;
        this.email = email;
        this.skillList = skillList;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Resume(UUID id, UUID userId, String title, String summary, Email email, List<Skill> skillList, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.summary = summary;
        this.email = email;
        this.skillList = skillList;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(String title, String summary, Email email, List<Skill> skillList) {
        this.title = title;
        this.summary = summary;
        this.email = email;
        this.skillList = skillList;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Resume resume = (Resume) o;
        return Objects.equals(userId, resume.userId) && Objects.equals(title, resume.title) && Objects.equals(createdAt, resume.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, title, createdAt);
    }
}