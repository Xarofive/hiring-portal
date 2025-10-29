package ru.kata.project.ai.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.ai.core.entity.EducationItem;
import ru.kata.project.ai.core.entity.ExperienceItem;
import ru.kata.project.ai.core.entity.GeneratedResume;
import ru.kata.project.ai.core.entity.GenerationContext;
import ru.kata.project.ai.core.port.ResumeGenerator;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenerateResumeUseCaseTest {

    @Mock
    private ResumeGenerator generator;

    @InjectMocks
    private GenerateResumeUseCase useCase;

    @Test
    void givenValidContext_whenExecute_thenReturnsGeneratedResume() {
        final GenerationContext context = new GenerationContext(
                "Ivan Ivanov",
                "Java Developer",
                List.of("Spring", "Docker"),
                List.of(),
                List.of(),
                Locale.ENGLISH
        );

        final GeneratedResume expected = new GeneratedResume(
                "Java Developer — Ivan Ivanov",
                "Summary text",
                List.of("Spring", "Docker"),
                List.of(),
                List.of(),
                "stub-1.0.0",
                Locale.ENGLISH
        );

        when(generator.generate(context)).thenReturn(expected);

        final GeneratedResume actual = useCase.execute(context);

        verify(generator).generate(context);
        assertThat(actual.templateVersion()).isEqualTo("stub-1.0.0");
        assertThat(actual.locale()).isEqualTo(Locale.ENGLISH);
        assertThat(actual.skills()).containsExactly("Spring", "Docker");
    }

    @Test
    void givenNullContext_whenExecute_thenThrowsException() {
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("контекст генерации не должен быть null");

        verifyNoInteractions(generator);
    }

    @Test
    void givenEmptyFullNameAndRole_whenExecute_thenThrowsException() {
        final GenerationContext context = new GenerationContext(
                "   ",
                "   ",
                List.of("Skill"),
                List.of(),
                List.of(),
                Locale.ENGLISH
        );

        assertThatThrownBy(() -> useCase.execute(context))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("не указаны имя и целевая роль");

        verifyNoInteractions(generator);
    }

    @Test
    void givenOnlyFullName_whenExecute_thenSucceeds() {
        final GenerationContext context = new GenerationContext(
                "Alice",
                null,
                List.of(),
                List.of(),
                List.of(),
                Locale.ENGLISH
        );

        final GeneratedResume expected = new GeneratedResume(
                "Specialist — Alice",
                "Draft resume",
                List.of(),
                List.of(),
                List.of(),
                "stub-1.0.0",
                Locale.ENGLISH
        );

        when(generator.generate(context)).thenReturn(expected);

        final GeneratedResume actual = useCase.execute(context);

        verify(generator).generate(context);
        assertThat(actual.title()).contains("Alice");
        assertThat(actual.templateVersion()).isEqualTo("stub-1.0.0");
    }

    @Test
    void givenOnlyTargetRole_whenExecute_thenSucceeds() {
        final GenerationContext context = new GenerationContext(
                null,
                "Data Scientist",
                List.of(),
                List.of(),
                List.of(),
                Locale.ENGLISH
        );

        final GeneratedResume expected = new GeneratedResume(
                "Data Scientist — Anonymous",
                "Some summary",
                List.of(),
                List.of(),
                List.of(),
                "stub-1.0.0",
                Locale.ENGLISH
        );

        when(generator.generate(context)).thenReturn(expected);

        final GeneratedResume actual = useCase.execute(context);

        verify(generator).generate(context);
        assertThat(actual.title()).contains("Data Scientist");
    }

    @Test
    void givenExperienceWithNullDates_whenExecute_thenThrowsException() {
        final ExperienceItem invalidExp = new ExperienceItem(
                "TechCorp",
                "Engineer",
                null,
                YearMonth.of(2024, 5),
                "Worked on stuff",
                List.of()
        );
        final GenerationContext context = new GenerationContext(
                "Ivan",
                "Developer",
                List.of("Java"),
                List.of(invalidExp),
                List.of(),
                Locale.ENGLISH
        );

        assertThatThrownBy(() -> useCase.execute(context))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("опыт работы")
                .hasMessageContaining("TechCorp");

        verifyNoInteractions(generator);
    }

    @Test
    void givenExperienceWithEndBeforeStart_whenExecute_thenThrowsException() {
        final ExperienceItem invalidExp = new ExperienceItem(
                "Acme Corp",
                "Tester",
                YearMonth.of(2023, 5),
                YearMonth.of(2022, 12),
                "Testing apps",
                List.of()
        );
        final GenerationContext context = new GenerationContext(
                "Bob",
                "QA Engineer",
                List.of(),
                List.of(invalidExp),
                List.of(),
                Locale.ENGLISH
        );

        assertThatThrownBy(() -> useCase.execute(context))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Acme Corp")
                .hasMessageContaining("окончания раньше даты начала");

        verifyNoInteractions(generator);
    }

    @Test
    void givenEducationWithEndBeforeStart_whenExecute_thenThrowsException() {
        final EducationItem invalidEdu = new EducationItem(
                "Bachelor",
                "MIT",
                Year.of(2025),
                Year.of(2023),
                "CS"
        );
        final GenerationContext context = new GenerationContext(
                "Alice",
                "Engineer",
                List.of(),
                List.of(),
                List.of(invalidEdu),
                Locale.ENGLISH
        );

        assertThatThrownBy(() -> useCase.execute(context))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("MIT")
                .hasMessageContaining("окончания раньше даты начала");

        verifyNoInteractions(generator);
    }

    @Test
    void givenEducationWithNullDates_whenExecute_thenThrowsException() {
        final EducationItem invalidEdu = new EducationItem(
                "B.Sc.",
                "Stanford",
                null,
                null,
                "CS"
        );
        final GenerationContext context = new GenerationContext(
                "Tom",
                "Developer",
                List.of(),
                List.of(),
                List.of(invalidEdu),
                Locale.ENGLISH
        );

        assertThatThrownBy(() -> useCase.execute(context))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stanford")
                .hasMessageContaining("неполные даты");

        verifyNoInteractions(generator);
    }

    @Test
    void givenNoExperienceAndNoEducationNull_whenExecute_thenSucceeds() {
        final GenerationContext context = new GenerationContext(
                "Alice",
                "QA Engineer",
                List.of("Testing"),
                null,
                null,
                Locale.ENGLISH
        );

        final GeneratedResume expected = new GeneratedResume(
                "QA Engineer — Alice",
                "Stub summary",
                List.of("Testing"),
                List.of(),
                List.of(),
                "stub-1.0.0",
                Locale.ENGLISH
        );

        when(generator.generate(context)).thenReturn(expected);

        final GeneratedResume actual = useCase.execute(context);

        verify(generator).generate(context);
        assertThat(actual.title()).contains("QA Engineer");
    }

    @Test
    void givenNoExperienceAndNoEducationEmpty_whenExecute_thenSucceeds() {
        final GenerationContext context = new GenerationContext(
                "Alice",
                "QA Engineer",
                List.of("Testing"),
                List.of(),
                List.of(),
                Locale.ENGLISH
        );

        final GeneratedResume expected = new GeneratedResume(
                "QA Engineer — Alice",
                "Stub summary",
                List.of("Testing"),
                List.of(),
                List.of(),
                "stub-1.0.0",
                Locale.ENGLISH
        );

        when(generator.generate(context)).thenReturn(expected);

        final GeneratedResume actual = useCase.execute(context);

        verify(generator).generate(context);
        assertThat(actual.title()).contains("QA Engineer");
    }
}