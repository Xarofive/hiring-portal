package ru.kata.project.resume.core.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.core.entity.vo.Email;
import ru.kata.project.resume.core.entity.vo.Skill;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.utility.ResumeExeption.ResumeNotFoundException;
import ru.kata.project.resume.utility.ResumeExeption.ResumeValidationException;
import ru.kata.project.resume.web.dto.ResumeRequestDTO;
import ru.kata.project.resume.web.dto.ResumeResponseDTO;
import ru.kata.project.security.utility.SecurityUtils;
import ru.kata.project.user.core.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.kata.project.resume.core.entity.vo.Skill.SkillLevel.EXPERT;
import static ru.kata.project.resume.core.entity.vo.Skill.SkillLevel.INTERMEDIATE;

@ExtendWith(MockitoExtension.class)
public class UpdateResumeUseCaseTest {
    @Mock
    ResumeRepository resumeRepository;

    @InjectMocks
    UpdateResumeUseCase updateResumeUseCase;

    private UUID userId;
    private UUID resumeId;
    private Resume resume;
    private Resume updateResume;
    private ResumeRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        resumeId = UUID.randomUUID();

        resume = Resume.builder()
                .id(resumeId)
                .userId(userId)
                .title("JavaScript Developer")
                .summary("Какое-то описание")
                .email(new Email("test@example.com"))
                .skillList(List.of(new Skill("JavaScript", INTERMEDIATE)))
                .build();

        updateResume = Resume.builder()
                .id(resumeId)
                .userId(userId)
                .title("JavaScript Developer")
                .summary("Какое-то описание")
                .email(new Email("test@example.com"))
                .skillList(List.of(new Skill("JavaScript", EXPERT)))
                .build();

        requestDTO = new ResumeRequestDTO(
                "JavaScript Developer",
                "Какое-то описание",
                new Email("test@example.com"),
                List.of(new Skill("JavaScript", EXPERT))
        );
    }

    @Test
    void execute_WithNullId_ShouldThrowValidationException() {
        assertThrows(ResumeValidationException.class, () -> updateResumeUseCase.execute(null, requestDTO));

        verify(resumeRepository, never()).findById(any());
    }

    @Test
    void execute_WithNullRequest_ShouldThrowValidationException() {
        assertThrows(ResumeValidationException.class, () -> updateResumeUseCase.execute(resumeId, null));

        verify(resumeRepository, never()).findById(any());
    }

    @Test
    void execute_WithInvalidId_ShouldThrowException() {
        when(resumeRepository.findById(resumeId)).thenReturn(Optional.empty());

        assertThrows(ResumeNotFoundException.class, () -> updateResumeUseCase.execute(resumeId, requestDTO));

        verify(resumeRepository).findById(any());
        verify(resumeRepository, never()).save(any());
    }

    @Test
    void execute_WithValidParameters_ShouldUpdateResume() {
        when(resumeRepository.findById(resumeId)).thenReturn(Optional.of(resume));
        when(resumeRepository.save(resume)).thenReturn(updateResume);

        try (MockedStatic<SecurityUtils> securityMock = mockStatic(SecurityUtils.class)) {
            securityMock.when(() -> SecurityUtils.checkOwnership(userId)).thenAnswer(invocation -> null);

            final ResumeResponseDTO responseDTO = updateResumeUseCase.execute(resumeId, requestDTO);

            assertNotNull(responseDTO);
            assertEquals(resumeId, responseDTO.id());
            assertEquals(requestDTO.title(), responseDTO.title());
            assertEquals(requestDTO.summary(), responseDTO.summary());
            assertThat(responseDTO.skillList())
                    .hasSize(1)  // или requestDTO.skillList().size()
                    .extracting(Skill::level)
                    .containsExactly(EXPERT);

            verify(resumeRepository).findById(resumeId);
            verify(resumeRepository).save(any(Resume.class));
        }
    }
}