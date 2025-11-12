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
import ru.kata.project.resume.utility.resumeExeption.ResumeValidationException;
import ru.kata.project.resume.web.dto.CreateResumeResponseDTO;
import ru.kata.project.resume.web.dto.ResumeRequestDTO;
import ru.kata.project.security.utility.SecurityUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.kata.project.resume.core.entity.vo.Skill.SkillLevel.INTERMEDIATE;

@ExtendWith(MockitoExtension.class)
class CreateResumeUseCaseTest {

    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private CreateResumeUseCase createResumeUseCase;

    private UUID userId;
    private ResumeRequestDTO requestDTO;
    private Resume savedResume;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        final UUID resumeId = UUID.randomUUID();

        requestDTO = ResumeRequestDTO.builder()
                .title("Java Developer")
                .summary("Какое-то описание")
                .email(new Email("test@example.com"))
                .skills(List.of(
                        new Skill("JavaScript", INTERMEDIATE),
                        new Skill("Spring Boot", INTERMEDIATE),
                        new Skill("Hibernate", INTERMEDIATE)))
                .build();

        savedResume = Resume.builder()
                .id(resumeId)
                .userId(userId)
                .title("JavaScript Developer")
                .summary("Какое-то описание")
                .email(new Email("test@example.com"))
                .skillList(List.of(new Skill("JavaScript", INTERMEDIATE)))
                .build();
    }

    @Test
    void execute_WithNullRequest_ShouldThrowResumeValidationException() {
        assertThrows(ResumeValidationException.class, () -> createResumeUseCase.execute(null));

        verify(resumeRepository, never()).save(any());
    }

    @Test
    void execute_WithValidRequest_ShouldCreateResume() {

        when(resumeRepository.save(any(Resume.class))).thenReturn(savedResume);

        try (MockedStatic<SecurityUtils> securityMock = mockStatic(SecurityUtils.class)) {
            securityMock.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            securityMock.when(() -> SecurityUtils.checkRole(SecurityUtils.ROLE_CANDIDATE))
                    .thenAnswer(invocation -> null);

            final CreateResumeResponseDTO response = createResumeUseCase.execute(requestDTO);

            assertNotNull(response);
            assertEquals(savedResume.getId(), response.id());
            assertEquals(savedResume.getCreatedAt(), response.createdAt());

            verify(resumeRepository).save(any(Resume.class));
        }
    }
}