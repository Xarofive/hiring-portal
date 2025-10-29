package ru.kata.project.ai.adapters.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.project.ai.adapters.rest.dto.AnalysisResultDto;
import ru.kata.project.ai.adapters.rest.dto.AnalyzeRequestDto;
import ru.kata.project.ai.adapters.rest.dto.GenerateRequestDto;
import ru.kata.project.ai.adapters.rest.dto.GeneratedResumeDto;
import ru.kata.project.ai.adapters.rest.mapper.GlobalControllerMapper;
import ru.kata.project.ai.application.AnalyzeResumeUseCase;
import ru.kata.project.ai.application.GenerateResumeUseCase;
import ru.kata.project.ai.core.entity.AnalysisResult;
import ru.kata.project.ai.core.entity.GeneratedResume;

/**
 * ResumeAiController
 * <p>
 * Контроллер для AI-модуля.
 * </p>
 * <p>
 * Реализует эндпоинты для анализа и генерации резюме с помощью ИИ.
 * </p>
 * <p>
 * Использует:
 *  <ul>
 *   <li> анализ существующего резюме для целевой позиции и целевого грейда через {@link AnalyzeResumeUseCase};</li>
 *   <li> генерация шаблона резюме по полученным данным через {@link GenerateResumeUseCase};</li>
 *  </ul>
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/resumes")
@Tag(name = "AI Resume", description = "Эндпоинты для анализа и генерации резюме с помощью ИИ.")
public class ResumeAiController {

    private final AnalyzeResumeUseCase analyzeResumeUseCase;
    private final GenerateResumeUseCase generateResumeUseCase;

    @Operation(
            summary = "Анализ резюме",
            description = "Выполняет анализ резюме по указанному идентификатору и возвращает рекомендации."
    )
    @PostMapping("/{id}/analyze")
    public ResponseEntity<AnalysisResultDto> analyze(
            @PathVariable("id") String id,
            @RequestBody(required = false) AnalyzeRequestDto dto) {
        final AnalysisResult result = analyzeResumeUseCase.execute(id, GlobalControllerMapper.toDomain(dto));
        return ResponseEntity.ok(GlobalControllerMapper.toDto(result));
    }

    @Operation(
            summary = "Генерация резюме",
            description = "Создаёт шаблон резюме по входным данным (имя, должность, навыки и т.д.)."
    )
    @PostMapping("/generate")
    public ResponseEntity<GeneratedResumeDto> generate(
            @RequestBody GenerateRequestDto dto) {
        final GeneratedResume resume = generateResumeUseCase.execute(GlobalControllerMapper.toDomain(dto));
        return ResponseEntity.ok(GlobalControllerMapper.toDto(resume));
    }
}