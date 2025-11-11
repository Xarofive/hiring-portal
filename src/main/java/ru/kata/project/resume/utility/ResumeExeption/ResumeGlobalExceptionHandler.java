package ru.kata.project.resume.utility.ResumeExeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ResumeGlobalExceptionHandler {

    @ExceptionHandler(ResumeValidationException.class)
    public ResponseEntity<ResumeErrorResponse> handleValidationException(ResumeValidationException ex) {
        log.warn("Ошибка валидации резюме: {}", ex.getMessage());
        final ResumeErrorResponse errorResponse = new ResumeErrorResponse("RESUME_VALIDATION_ERROR", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ResumeNotFoundException.class)
    public ResponseEntity<ResumeErrorResponse> handleNotFoundException(ResumeNotFoundException ex) {
        log.warn("Ошибка поиска данных: {}", ex.getMessage());
        final ResumeErrorResponse errorResponse = new ResumeErrorResponse("RESUME_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ResumeDataException.class)
    public ResponseEntity<ResumeErrorResponse> handleResumeDataException(ResumeDataException ex) {
        log.error("Ошибка БД: {}, resumeId = {}, userId = {}",
                ex.getMessage(), ex.getResumeId(), ex.getUserId(), ex);
        final ResumeErrorResponse errorResponse = new ResumeErrorResponse("RESUME_DATA_ERROR", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}