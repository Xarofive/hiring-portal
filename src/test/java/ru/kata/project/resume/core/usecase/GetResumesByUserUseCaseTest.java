package ru.kata.project.resume.core.usecase;

import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.utility.ResumeExeption.ResumeNotFoundException;
import ru.kata.project.resume.utility.ResumeExeption.ResumeValidationException;
import ru.kata.project.resume.web.dto.ResumeResponseDTO;
import ru.kata.project.security.utility.SecurityUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetResumesByUserUseCaseTest {
    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private GetResumesByUserUseCase getResumesByUserUseCase;

    UUID userId = UUID.randomUUID();

    @Test
    void execute_WithNullId_ShouldThrowValidationException() {
        assertThrows(ResumeValidationException.class, () -> getResumesByUserUseCase.execute(null));

        verify(resumeRepository, never()).findById(any());
    }

    @Test
    void execute_WithValidId_ShouldReturnListResumes() {
        userId = UUID.randomUUID();
        final List<Resume> resumes = List.of(
                Resume.builder().id(UUID.randomUUID()).build(),
                Resume.builder().id(UUID.randomUUID()).build());

        when(resumeRepository.findByUserId(userId)).thenReturn(resumes);

        try (MockedStatic<SecurityUtils> securityMock = mockStatic(SecurityUtils.class)) {
            securityMock.when(() -> SecurityUtils.isCurrentUserOwner(userId)).thenReturn(true);
            securityMock.when(() -> SecurityUtils.hasRole(SecurityUtils.ROLE_ADMIN)).thenReturn(true);

            final List<ResumeResponseDTO> resumesDTO = getResumesByUserUseCase.execute(userId);

            assertFalse(resumesDTO.isEmpty());
            assertEquals(2, resumesDTO.size());
        }
    }

    @Test
    void execute_WithEmptyList_ShouldThrowException() {
        userId = UUID.randomUUID();

        when(resumeRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        try (MockedStatic<SecurityUtils> securityMock = mockStatic(SecurityUtils.class)) {
            securityMock.when(() -> SecurityUtils.isCurrentUserOwner(userId)).thenReturn(true);
            securityMock.when(() -> SecurityUtils.hasRole(SecurityUtils.ROLE_ADMIN)).thenReturn(true);

            assertThrows(ResumeNotFoundException.class, () -> getResumesByUserUseCase.execute(userId));

            verify(resumeRepository).findByUserId(userId);
        }
    }
}