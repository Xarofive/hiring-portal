package ru.kata.project.resume.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.persistence.jpa.entity.voEntity.EmailEmbeddable;
import ru.kata.project.resume.persistence.jpa.entity.voEntity.SkillEmbeddable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
/**
 * ResumeEntity
 * <p>
 *     JPA сущность для Resume
 * </p>
 */

@Entity
@Table(name = "resumes")
@Getter
@Setter
public class ResumeEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "summary", nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Embedded
    private EmailEmbeddable email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resume_skills", joinColumns = @JoinColumn(name = "resume_id"))
    private List<SkillEmbeddable> skills = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ResumeEntity() {
    }

    public ResumeEntity(Resume resume) {
        this.id = resume.getId();
        this.userId = resume.getUserId();
        this.title = resume.getTitle();
        this.summary = resume.getSummary();
        this.email = EmailEmbeddable.fromDomain(resume.getEmail());
        this.skills = resume.getSkillList().stream()
                .map(SkillEmbeddable::fromDomain)
                .collect(Collectors.toList());
        this.createdAt = resume.getCreatedAt();
        this.updatedAt = resume.getUpdatedAt();
    }

    public Resume toDomain() {
        return new Resume(
                this.id,
                this.userId,
                this.title,
                this.summary,
                this.email.toDomain(),
                this.skills.stream()
                        .map(SkillEmbeddable::toDomain)
                        .collect(Collectors.toList()),
                this.createdAt,
                this.updatedAt
        );
    }
}