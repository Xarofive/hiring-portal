package ru.kata.project.shared.utility.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kata.project.shared.utility.exception.BadUserStatusException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class CustomUnconfirmedUserStatusHandler {

    @ExceptionHandler(BadUserStatusException.class)
    public ResponseEntity<Map<String, Object>> handleUnconfirmedUser(BadUserStatusException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.FORBIDDEN.value(),
                        "error", "User not confirmed",
                        "message", ex.getMessage()
                ));
    }

}
