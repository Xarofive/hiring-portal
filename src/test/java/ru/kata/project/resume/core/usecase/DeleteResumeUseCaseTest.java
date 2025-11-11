package ru.kata.project.resume.core.usecase;

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
import ru.kata.project.security.utility.SecurityUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteResumeUseCaseTest {
    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private DeleteResumeUseCase deleteResumeUseCase;

    @Test
    void execute_WithNullId_ShouldThrowValidationException() {
        assertThrows(ResumeValidationException.class, () -> deleteResumeUseCase.execute(null));

        verify(resumeRepository, never()).findById(any());
    }

    @Test
    void execute_WithValidId_ShouldDeleteResume() {
        final UUID userId = UUID.randomUUID();
        final UUID resumeId = UUID.randomUUID();

        when(resumeRepository.findById(resumeId)).thenReturn(Optional.of(Resume.builder().id(resumeId).build()));
        when(resumeRepository.deleteById(resumeId)).thenReturn(true);

        try (MockedStatic<SecurityUtils> securityMock = mockStatic(SecurityUtils.class)) {
            securityMock.when(() -> SecurityUtils.isCurrentUserOwner(userId)).thenReturn(true);
            securityMock.when(() -> SecurityUtils.hasRole(SecurityUtils.ROLE_ADMIN)).thenReturn(true);

            deleteResumeUseCase.execute(resumeId);

            verify(resumeRepository).findById(any());
            verify(resumeRepository).deleteById(any());
        }
    }

    @Test
    void execute_WithInvalidId_ShouldThrowException() {
        final UUID resumeId = UUID.randomUUID();

        when(resumeRepository.findById(resumeId)).thenReturn(Optional.empty());

        assertThrows(ResumeNotFoundException.class, () -> deleteResumeUseCase.execute(resumeId));

        verify(resumeRepository, never()).deleteById(any());
    }
}