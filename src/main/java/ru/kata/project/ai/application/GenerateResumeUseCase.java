package ru.kata.project.ai.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kata.project.ai.core.entity.GeneratedResume;
import ru.kata.project.ai.core.entity.GenerationContext;
import ru.kata.project.ai.core.port.ResumeGenerator;

/**
 * GenerateResumeUseCase
 * <p>
 * Use-case для обработки сценария "Генерация резюме".
 * </p>
 * <p>
 * Этот класс выполняет генерацию резюме пользователя по предоставленным данным.
 * </p>
 * <ul>
 *  <li> генерация резюме через генератор {@link ResumeGenerator};</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@Slf4j
@AllArgsConstructor
public class GenerateResumeUseCase {

    private final ResumeGenerator resumeGenerator;

    public GeneratedResume execute(GenerationContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Ошибка: контекст генерации не должен быть null");
        }
        if ((context.fullName() == null || context.fullName().isBlank())
                && (context.targetRole() == null || context.targetRole().isBlank())) {
            throw new IllegalArgumentException("Ошибка: не указаны имя и целевая роль — хотя бы одно из полей должно быть заполнено");
        }

        log.info("Запуск генерации резюме: роль = '{}'", context.targetRole());

        final GeneratedResume result = resumeGenerator.generate(context);

        log.info("Генерация завершена. Версия шаблона: '{}'", result.templateVersion());

        return result;
    }
}