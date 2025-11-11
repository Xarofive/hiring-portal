package ru.kata.project.ai.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kata.project.ai.application.exception.ResourceNotFoundException;
import ru.kata.project.ai.application.port.ResumeRepository;
import ru.kata.project.ai.core.entity.AnalysisContext;
import ru.kata.project.ai.core.entity.AnalysisResult;
import ru.kata.project.ai.core.entity.ResumeId;
import ru.kata.project.ai.core.port.ResumeAnalyzer;

import java.time.Instant;

/**
 * AnalyzeResumeUseCase
 * <p>
 * Use-case для обработки сценария "Анализ резюме".
 * </p>
 * <p>
 * Этот класс выполняет анализ резюме пользователя по предоставленным данным.
 * </p>
 * <ul>
 *  <li> анализ резюме через анализатор {@link ResumeAnalyzer};</li>
 *  <li> проверка существования резюме через {@link ResumeRepository};</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@Slf4j
@AllArgsConstructor
public class AnalyzeResumeUseCase {

    private final ResumeAnalyzer analyzer;
    private final ResumeRepository resumeRepository;

    public AnalysisResult execute(String idStr, AnalysisContext analysisContext) {
        log.info("Анализ резюме, id = {}", idStr);
        final ResumeId id = validateResume(idStr);

        final AnalysisResult result = analyzer.analyze(id, analysisContext);

        log.info("Анализ резюме завершён, id = {}", idStr);
        return new AnalysisResult(result.resumeId(), result.recommendations(), Instant.now(), result.analyzerVersion());
    }

    private ResumeId validateResume(String id) {
        final ResumeId resumeId;
        try {
            resumeId = ResumeId.of(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Неверный идентификатор резюме: " + id, e);
        }
        if (!resumeRepository.existsById(resumeId.getValue())) {
            throw new ResourceNotFoundException("Резюме не найдено: " + id);
        }
        return resumeId;
    }
}