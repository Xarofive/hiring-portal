package ru.kata.project.ai.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.ai.core.entity.GeneratedResume;
import ru.kata.project.ai.core.entity.GenerationContext;
import ru.kata.project.ai.core.port.ResumeGenerator;

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
}