package ru.kata.project.user.shared.utility.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * CustomAuthExceptionHandler
 * <p>
 * Обработчик ошибок, связанных с некорректным статусом пользователя.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@RestControllerAdvice
public class CustomAuthExceptionHandler {

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabled(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("У данного аккаунта неподтверждённая почта. Пожалуйста, подтвердите почту.");
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<String> handleLocked(LockedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Пользователь заблокирован.");
    }
}
