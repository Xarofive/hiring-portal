package ru.kata.project.ai.application.exception;

import ru.kata.project.ai.adapters.rest.handler.GlobalExceptionHandler;

import java.io.Serial;

/**
 * ResourceNotFoundException
 * <p>
 * Исключение, выбрасываемое при отсутствии запрашиваемого ресурса.
 * </p>
 * <p>
 * Используется для сигнализации о том, что объект (например, резюме, пользователь или запись в БД)
 * не найден по указанному идентификатору или критериям поиска.
 * </p>
 * <p>
 * Обрабатывается с помощью {@link GlobalExceptionHandler}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1553394163088578939L;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
