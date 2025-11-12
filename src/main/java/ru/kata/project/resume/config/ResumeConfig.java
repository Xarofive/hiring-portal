package ru.kata.project.resume.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.core.usecase.*;
/**
 * ResumeConfig
 * <p>
 *     Конфигурация Spring для Use Cases и репозиториев
 * </p>
 */

@Configuration
public class ResumeConfig {

    @Bean
    public CreateResumeUseCase createResumeUseCase(ResumeRepository resumeRepository) {
        return new CreateResumeUseCase(resumeRepository);
    }

    @Bean
    public GetResumeByIdUseCase getResumeByIdUseCase(ResumeRepository resumeRepository) {
        return new GetResumeByIdUseCase(resumeRepository);
    }

    @Bean
    public GetResumesByUserUseCase getResumeByUserUseCase(ResumeRepository resumeRepository) {
        return new GetResumesByUserUseCase(resumeRepository);
    }

    @Bean
    public UpdateResumeUseCase updateResumeUseCase(ResumeRepository resumeRepository) {
        return new UpdateResumeUseCase(resumeRepository);
    }

    @Bean
    public DeleteResumeUseCase deleteResumeUseCase(ResumeRepository resumeRepository) {
        return new DeleteResumeUseCase(resumeRepository);
    }
}
