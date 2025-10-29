package ru.kata.project.ai.adapters.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.ai.core.entity.AnalysisContext;
import ru.kata.project.ai.core.entity.AnalysisResult;
import ru.kata.project.ai.core.entity.ResumeId;
import ru.kata.project.ai.core.entity.Section;
import ru.kata.project.ai.core.entity.Seniority;
import ru.kata.project.ai.core.entity.Severity;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StubResumeAnalyzerTest {

    @InjectMocks
    private StubResumeAnalyzer analyzer;

    private ResumeId id;
    private AnalysisContext russianContext;
    private AnalysisContext englishContext;

    @BeforeEach
    void init() {
        id = ResumeId.of(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        englishContext = new AnalysisContext(Locale.ENGLISH, "Java Developer", Seniority.MIDDLE);
        russianContext = new AnalysisContext(Locale.forLanguageTag("ru"), "Java Developer", Seniority.SENIOR);
    }

    @Test
    void givenValidContext_withEnglishLocale_whenExecute_thenReturnStubRecommendations() {
        final AnalysisResult result = analyzer.analyze(id, englishContext);

        assertThat(result.resumeId()).isEqualTo(id);
        assertThat(result.analyzerVersion()).isEqualTo("stub-1.0.0");
        assertThat(result.analyzedAt()).isBeforeOrEqualTo(Instant.now());
        assertThat(result.recommendations()).hasSize(3);

        assertThat(result.recommendations().get(0).code()).isEqualTo("SKILLS_TOO_BROAD");
        assertThat(result.recommendations().get(1).severity()).isEqualTo(Severity.MEDIUM);
        assertThat(result.recommendations().get(2).section()).isEqualTo(Section.EXPERIENCE);

        assertThat(result.recommendations().get(0).message())
                .isEqualTo("Skills are too general, prefer concrete technologies");
        assertThat(result.recommendations().get(1).message())
                .isEqualTo("Summary is short or missing");
        assertThat(result.recommendations().get(2).message())
                .isEqualTo("Consider expanding experience descriptions with metrics");
    }

    @Test
    void givenValidContext_withRussianLocale_whenExecute_thenReturnStubRecommendations() {
        final AnalysisResult result = analyzer.analyze(id, russianContext);

        assertThat(result.resumeId()).isEqualTo(id);
        assertThat(result.analyzerVersion()).isEqualTo("stub-1.0.0");
        assertThat(result.analyzedAt()).isBeforeOrEqualTo(Instant.now());
        assertThat(result.recommendations()).hasSize(3);

        assertThat(result.recommendations().get(0).code()).isEqualTo("SKILLS_TOO_BROAD");
        assertThat(result.recommendations().get(1).severity()).isEqualTo(Severity.MEDIUM);
        assertThat(result.recommendations().get(2).section()).isEqualTo(Section.EXPERIENCE);

        assertThat(result.recommendations().get(0).message())
                .isEqualTo("Навыки слишком общие, укажите конкретные технологии");
        assertThat(result.recommendations().get(1).message())
                .isEqualTo("Краткое описание отсутствует или слишком короткое");
        assertThat(result.recommendations().get(2).message())
                .isEqualTo("Рекомендуется расширить описание опыта с метриками");
    }
}