package ru.kata.project.ai.adapters.rest.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kata.project.ai.adapters.rest.ResumeAiController;
import ru.kata.project.ai.application.exception.ResourceNotFoundException;

/**
 * GlobalExceptionHandler
 * <p>
 * Глобальный обработчик ошибок для контроллера AI-модуля {@link ResumeAiController}.
 * </p>
 * <p>
 * Класс {@link ErrorResponse} используется для точного ответа пользователю о причине ошибки.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@ControllerAdvice(basePackages = "ru.kata.project.ai.adapters.rest")
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("bad_request", ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("not_found", ex.getMessage()));
    }

    record ErrorResponse(String code, String message) {
    }
}