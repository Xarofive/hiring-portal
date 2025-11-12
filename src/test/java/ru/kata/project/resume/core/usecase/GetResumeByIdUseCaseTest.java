package ru.kata.project.resume.core.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.core.entity.vo.Email;
import ru.kata.project.resume.core.entity.vo.Skill;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.utility.resumeExeption.ResumeNotFoundException;
import ru.kata.project.resume.utility.resumeExeption.ResumeValidationException;
import ru.kata.project.resume.web.dto.ResumeResponseDTO;
import ru.kata.project.security.utility.SecurityUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.kata.project.resume.core.entity.vo.Skill.SkillLevel.INTERMEDIATE;

@ExtendWith(MockitoExtension.class)
public class GetResumeByIdUseCaseTest {
    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private GetResumeByIdUseCase getResumeByIdUseCase;

    private UUID resumeId;

    @Test
    void execute_WithNullId_ShouldThrowValidationException() {
        assertThrows(ResumeValidationException.class, () -> getResumeByIdUseCase.execute(null));

        verify(resumeRepository, never()).findById(any());
    }

    @Test
    void execute_WithExistingResumeId_ShouldReturnResume() {
        resumeId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();

        final Resume resume = Resume.builder()
                .id(resumeId)
                .userId(userId)
                .title("Java Developer")
                .summary("Какое-то описание")
                .email(new Email("test@example.com"))
                .skillList(List.of(
                        new Skill("Java", INTERMEDIATE),
                        new Skill("Spring Boot", INTERMEDIATE)
                ))
                .build();
        when(resumeRepository.findById(resumeId)).thenReturn(Optional.of(resume));

        try (MockedStatic<SecurityUtils> securityMock = mockStatic(SecurityUtils.class)) {
            final Authentication auth = mock(Authentication.class);
            securityMock.when(SecurityUtils::getAuthentication).thenReturn(auth);

            final ResumeResponseDTO responseDTO = getResumeByIdUseCase.execute(resumeId);

            assertThat(responseDTO).isNotNull();
            assertThat(responseDTO.id()).isEqualTo(resumeId);
            assertThat(responseDTO.title()).isEqualTo("Java Developer");
            assertThat(responseDTO.summary()).isEqualTo("Какое-то описание");
            assertThat(responseDTO.email().value()).isEqualTo("test@example.com");
            assertThat(responseDTO.skills()).hasSize(2);
        }
    }

    @Test
    void execute_WithNonExistingResumeId_ShouldThrowResumeNotFoundException() {
        resumeId = UUID.randomUUID();

        when(resumeRepository.findById(resumeId)).thenReturn(Optional.empty());

        try (MockedStatic<SecurityUtils> securityMock = mockStatic(SecurityUtils.class)) {
            final Authentication auth = mock(Authentication.class);
            securityMock.when(SecurityUtils::getAuthentication).thenReturn(auth);

            assertThrows(ResumeNotFoundException.class, () -> getResumeByIdUseCase.execute(resumeId));

            verify(resumeRepository).findById(resumeId);
        }
    }
}