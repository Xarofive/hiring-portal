package ru.kata.project.ai.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.project.ai.adapters.stub.StubResumeAnalyzer;
import ru.kata.project.ai.adapters.stub.StubResumeGenerator;
import ru.kata.project.ai.core.port.ResumeAnalyzer;
import ru.kata.project.ai.core.port.ResumeGenerator;

/**
 * AiStubConfig
 * <p>
 * Класс-конфигуратор для stub-бинов.
 * </p>
 * <p>
 * Если провайдер указан "stub", используются заглушки, указанные в этом классе, для генерации и анализа резюме.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Configuration
@ConditionalOnProperty(prefix = "ai", name = "provider", havingValue = "stub", matchIfMissing = true)
public class AiStubConfig {

    @Bean
    @ConditionalOnMissingBean
    public ResumeAnalyzer stubResumeAnalyzer() {
        return new StubResumeAnalyzer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResumeGenerator stubResumeGenerator() {
        return new StubResumeGenerator();
    }
}