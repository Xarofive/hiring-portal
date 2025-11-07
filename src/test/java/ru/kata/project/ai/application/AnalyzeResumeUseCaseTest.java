package ru.kata.project.ai.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.ai.application.exception.ResourceNotFoundException;
import ru.kata.project.ai.application.port.OutboxResumeEventPublisher;
import ru.kata.project.ai.application.port.ResumeRepository;
import ru.kata.project.ai.core.entity.AnalysisContext;
import ru.kata.project.ai.core.entity.AnalysisResult;
import ru.kata.project.ai.core.entity.ResumeId;
import ru.kata.project.ai.core.entity.Seniority;
import ru.kata.project.ai.core.port.ResumeAnalyzer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyzeResumeUseCaseTest {

    @Mock
    OutboxResumeEventPublisher outboxResumeEventPublisher;
    @Mock
    private ResumeAnalyzer analyzer;
    @Mock
    private ResumeRepository repository;
    @InjectMocks
    private AnalyzeResumeUseCase useCase;

    @Test
    void givenExistingResume_whenAnalyze_thenReturnsResult() {
        final String validId = "11111111-1111-1111-1111-111111111111";
        final AnalysisContext context = new AnalysisContext(java.util.Locale.ENGLISH, "Java Developer", Seniority.MIDDLE);
        final Instant oldTime = Instant.parse("2025-01-01T00:00:00Z");
        final ResumeId expectedId = ResumeId.of(UUID.fromString(validId));

        final AnalysisResult analyzerResult = new AnalysisResult(expectedId, List.of(), oldTime, "stub-1.0.0");

        when(repository.existsById(any())).thenReturn(true);
        when(analyzer.analyze(any(), eq(context))).thenReturn(analyzerResult);

        final AnalysisResult actual = useCase.execute(validId, context);

        verify(repository).existsById(any());
        verify(analyzer).analyze(any(), eq(context));

        assertThat(actual.resumeId()).isEqualTo(expectedId);
        assertThat(actual.recommendations()).isEmpty();
        assertThat(actual.analyzerVersion()).isEqualTo("stub-1.0.0");
        assertThat(actual.analyzedAt()).isAfter(oldTime);
        assertThat(actual.analyzedAt()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    void givenInvalidUuid_whenAnalyze_thenThrowsIllegalArgumentException() {
        final String invalidId = "not-a-uuid";
        final AnalysisContext context = new AnalysisContext(java.util.Locale.ENGLISH, "QA", Seniority.JUNIOR);

        assertThatThrownBy(() -> useCase.execute(invalidId, context))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Неверный идентификатор резюме");

        verifyNoInteractions(analyzer);
    }

    @Test
    void givenNonExistingResume_whenAnalyze_thenThrows404() {
        final String validId = UUID.randomUUID().toString();
        final ResumeId resumeId = ResumeId.of(UUID.fromString(validId));
        final AnalysisContext context = new AnalysisContext(java.util.Locale.ENGLISH, "DevOps", Seniority.SENIOR);

        when(repository.existsById(resumeId.getValue())).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(validId, context))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Резюме не найдено");

        verify(repository).existsById(resumeId.getValue());
        verifyNoInteractions(analyzer);
    }
}