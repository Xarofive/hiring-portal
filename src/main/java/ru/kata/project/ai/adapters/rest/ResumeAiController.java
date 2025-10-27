package ru.kata.project.ai.adapters.rest;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/resumes")
public class ResumeAiController {

    private final AnalyzeResumeUseCase analyzeResumeUseCase;
    private final GenerateResumeUseCase generateResumeUseCase;

    @PostMapping("/{id}/analyze")
    public ResponseEntity<AnalysisResultDto> analyze(
            @PathVariable("id") String id,
            @RequestBody(required = false) AnalyzeRequestDto dto) {
        final AnalysisResult result = analyzeResumeUseCase.execute(id, GlobalControllerMapper.toDomain(dto));
        return ResponseEntity.ok(GlobalControllerMapper.toDto(result));
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedResumeDto> generate(@RequestBody GenerateRequestDto dto) {
        final GeneratedResume resume = generateResumeUseCase.execute(GlobalControllerMapper.toDomain(dto));
        return ResponseEntity.ok(GlobalControllerMapper.toDto(resume));
    }
}