package ru.kata.project.ai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.project.ai.adapters.stub.StubResumeRepository;
import ru.kata.project.ai.application.AnalyzeResumeUseCase;
import ru.kata.project.ai.application.GenerateResumeUseCase;
import ru.kata.project.ai.application.port.ResumeRepository;
import ru.kata.project.ai.core.port.ResumeAnalyzer;
import ru.kata.project.ai.core.port.ResumeGenerator;

@Slf4j
@Configuration
public class AiBeanConfig {

    @Bean
    public AnalyzeResumeUseCase analyzeResumeUseCase(ResumeAnalyzer analyzer, ResumeRepository resumePort) {
        return new AnalyzeResumeUseCase(analyzer, resumePort);
    }

    @Bean
    public GenerateResumeUseCase generateResumeUseCase(ResumeGenerator generator) {
        return new GenerateResumeUseCase(generator);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResumeRepository stubResumePort() {
        log.warn("Не найден ResumeRepository, будет использована stub-версия.");
        return new StubResumeRepository();
    }
}