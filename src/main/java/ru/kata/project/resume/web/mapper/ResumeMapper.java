package ru.kata.project.resume.web.mapper;

import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.web.dto.CreateResumeResponseDTO;
import ru.kata.project.resume.web.dto.ResumeResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ResumeMapper
 * <p>
 *     Маппер для преобразования между доменными сущностями и DTO
 * </p>
 */
public class ResumeMapper {

    public static CreateResumeResponseDTO toCreateResumeResponseDTO(Resume resume) {
        return new CreateResumeResponseDTO(
                resume.getId(),
                resume.getCreatedAt());
    }

    public static ResumeResponseDTO toResumeResponseDTO(Resume resume) {
        return new ResumeResponseDTO(
                resume.getId(),
                resume.getUserId(),
                resume.getTitle(),
                resume.getSummary(),
                resume.getEmail(),
                resume.getSkillList(),
                resume.getCreatedAt(),
                resume.getUpdatedAt()
        );
    }

    public static List<ResumeResponseDTO> toResumeResponseDTOList(List<Resume> resumes) {
        return resumes.stream()
                .map(ResumeMapper::toResumeResponseDTO)
                .collect(Collectors.toList());
    }
}