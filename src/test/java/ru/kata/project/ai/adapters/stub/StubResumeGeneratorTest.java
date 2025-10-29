package ru.kata.project.ai.adapters.stub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.ai.core.entity.GeneratedResume;
import ru.kata.project.ai.core.entity.GenerationContext;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StubResumeGeneratorTest {

    @InjectMocks
    private StubResumeGenerator generator;

    @Test
    void givenValidContext_whenGenerate_thenReturnStubResume() {
        final GenerationContext ctx = new GenerationContext(
                "Ivan Ivanov",
                "Java Developer",
                List.of("Java", "Spring", "SQL"),
                List.of(),
                List.of(),
                Locale.ENGLISH);

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.templateVersion()).isEqualTo("stub-1.0.0");
        assertThat(resume.title()).contains("Java Developer", "Ivan Ivanov");
        assertThat(resume.summary()).contains("Draft resume for the role of Java Developer");
        assertThat(resume.skills()).containsExactly("Java", "Spring", "SQL");
    }

    @Test
    void givenNullFullName_whenGenerate_thenUsesDefaultName() {
        final GenerationContext ctx = new GenerationContext(
                null, "Tester", List.of("JUnit"), List.of(), List.of(), Locale.ENGLISH);

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.title()).contains("Anonymous Candidate");
        assertThat(resume.summary()).contains("Anonymous Candidate");
    }

    @Test
    void givenBlankFullName_whenGenerate_thenUsesDefaultName() {
        final GenerationContext ctx = new GenerationContext(
                "   ", "DevOps", List.of("Docker"), List.of(), List.of(), Locale.ENGLISH);

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.title()).contains("Anonymous Candidate");
    }

    @Test
    void givenNullRole_whenGenerate_thenUsesDefaultRole() {
        final GenerationContext ctx = new GenerationContext(
                "Alex", null, List.of("Python"), List.of(), List.of(), Locale.ENGLISH);

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.title()).contains("Specialist");
    }

    @Test
    void givenBlankRole_whenGenerate_thenUsesDefaultRole() {
        final GenerationContext ctx = new GenerationContext(
                "Alex", "   ", List.of("C++"), List.of(), List.of(), Locale.ENGLISH);

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.title()).contains("Specialist");
    }

    @Test
    void givenNullSkills_whenGenerate_thenUsesDefaultSkills() {
        final GenerationContext ctx = new GenerationContext(
                "Alex", "Engineer", null, List.of(), List.of(), Locale.ENGLISH);

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.skills()).containsExactly("Communication", "Teamwork");
    }

    @Test
    void givenEmptySkills_whenGenerate_thenUsesDefaultSkills() {
        final GenerationContext ctx = new GenerationContext(
                "Alex", "Engineer", List.of(), List.of(), List.of(), Locale.ENGLISH);

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.skills()).containsExactly("Communication", "Teamwork");
    }

    @Test
    void givenNullExperience_whenGenerate_thenSetsEmptyList() {
        final GenerationContext ctx = new GenerationContext(
                "Alex", "Engineer", List.of("Java"), null, List.of(), Locale.ENGLISH);

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.experience()).isEmpty();
    }

    @Test
    void givenNullEducation_whenGenerate_thenSetsEmptyList() {
        final GenerationContext ctx = new GenerationContext(
                "Alex", "Engineer", List.of("Java"), List.of(), null, Locale.ENGLISH);

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.education()).isEmpty();
    }

    @Test
    void givenRussianLocale_whenGenerate_thenBuildsRussianSummary() {
        final GenerationContext ctx = new GenerationContext(
                "Иван Иванов", "Тестировщик", List.of("JUnit"), List.of(), List.of(), Locale.forLanguageTag("ru"));

        final GeneratedResume resume = generator.generate(ctx);

        assertThat(resume.summary()).contains("Черновик резюме для роли");
        assertThat(resume.summary()).contains("Иван Иванов");
    }
}