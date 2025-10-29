package ru.kata.project.ai.adapters.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kata.project.ai.adapters.rest.dto.AnalyzeRequestDto;
import ru.kata.project.ai.adapters.rest.dto.GenerateRequestDto;
import ru.kata.project.ai.application.AnalyzeResumeUseCase;
import ru.kata.project.ai.application.GenerateResumeUseCase;
import ru.kata.project.ai.core.entity.AnalysisContext;
import ru.kata.project.ai.core.entity.AnalysisResult;
import ru.kata.project.ai.core.entity.GeneratedResume;
import ru.kata.project.ai.core.entity.GenerationContext;
import ru.kata.project.ai.core.entity.Recommendation;
import ru.kata.project.ai.core.entity.ResumeId;
import ru.kata.project.ai.core.entity.Section;
import ru.kata.project.ai.core.entity.Seniority;
import ru.kata.project.ai.core.entity.Severity;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ResumeAiController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResumeAiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AnalyzeResumeUseCase analyzeUseCase;

    @MockitoBean
    private GenerateResumeUseCase generateUseCase;

    @Test
    @DisplayName("POST /ai/resumes/{id}/analyze — returns AnalysisResultDto with structured recommendations")
    void analyzeResume_shouldReturnOk() throws Exception {
        final String id = UUID.randomUUID().toString();

        final AnalyzeRequestDto request = new AnalyzeRequestDto(
                "en",
                "Java Developer",
                Seniority.MIDDLE
        );

        final Recommendation rec = new Recommendation(
                "MISSING_PROJECTS",
                Severity.MEDIUM,
                "Add more project examples to your resume",
                Section.EXPERIENCE,
                Map.of("hint", "Include 2-3 recent projects")
        );

        final AnalysisResult result = new AnalysisResult(
                ResumeId.of(UUID.fromString(id)),
                List.of(rec),
                Instant.parse("2025-10-26T00:00:00Z"),
                "stub-1.0.0"
        );

        when(analyzeUseCase.execute(any(String.class), any(AnalysisContext.class)))
                .thenReturn(result);

        mockMvc.perform(post("/ai/resumes/{id}/analyze", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resumeId.value").value(id))
                .andExpect(jsonPath("$.recommendations[0].code").value("MISSING_PROJECTS"))
                .andExpect(jsonPath("$.recommendations[0].severity").value("MEDIUM"))
                .andExpect(jsonPath("$.recommendations[0].message").value("Add more project examples to your resume"))
                .andExpect(jsonPath("$.recommendations[0].section").value("EXPERIENCE"))
                .andExpect(jsonPath("$.recommendations[0].meta.hint").value("Include 2-3 recent projects"))
                .andExpect(jsonPath("$.analyzerVersion").value("stub-1.0.0"));
    }

    @Test
    @DisplayName("POST /ai/resumes/generate — returns GeneratedResumeDto")
    void generateResume_shouldReturnOk() throws Exception {
        final GenerateRequestDto request = new GenerateRequestDto(
                "Alice",
                "Java Developer",
                List.of("Spring", "Docker"),
                List.of(),
                List.of(),
                "en"
        );

        final GeneratedResume result = new GeneratedResume(
                "Java Developer — Alice",
                "A professional resume",
                List.of("Spring", "Docker"),
                List.of(),
                List.of(),
                "stub-1.0.0",
                Locale.ENGLISH
        );

        when(generateUseCase.execute(any(GenerationContext.class))).thenReturn(result);

        mockMvc.perform(post("/ai/resumes/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Developer — Alice"))
                .andExpect(jsonPath("$.templateVersion").value("stub-1.0.0"))
                .andExpect(jsonPath("$.skills[0]").value("Spring"))
                .andExpect(jsonPath("$.skills[1]").value("Docker"));
    }
}