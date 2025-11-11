package ru.kata.project.resume.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.project.resume.core.usecase.GetResumesByUserUseCase;
import ru.kata.project.resume.web.dto.ResumeResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * UserResumeController
 * <p>
 *  REST контроллер для управления резюме пользователя
 *  операции с резюме текущего пользователя</p>
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResumeController {

    private final GetResumesByUserUseCase getResumesByUserUseCase;

    @GetMapping("/{id}/resumes")
    public ResponseEntity<List<ResumeResponseDTO>> getResumesByUserId(@PathVariable UUID id) {
        final List<ResumeResponseDTO> responseList = getResumesByUserUseCase.execute(id);
        return ResponseEntity.ok(responseList);
    }
}