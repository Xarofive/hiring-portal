package ru.kata.project.resume.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.project.resume.core.usecase.CreateResumeUseCase;
import ru.kata.project.resume.core.usecase.DeleteResumeUseCase;
import ru.kata.project.resume.core.usecase.GetResumeByIdUseCase;
import ru.kata.project.resume.core.usecase.UpdateResumeUseCase;
import ru.kata.project.resume.web.dto.CreateResumeResponseDTO;
import ru.kata.project.resume.web.dto.ResumeRequestDTO;
import ru.kata.project.resume.web.dto.ResumeResponseDTO;

import java.util.UUID;

/**
 * ResumeController
 * <p>
 *     REST контроллер для управления резюме
 *     общие операции с резюме
 * </p>
 */
@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final CreateResumeUseCase createResumeUseCase;
    private final GetResumeByIdUseCase getResumeByIdUseCase;
    private final UpdateResumeUseCase updateResumeUseCase;
    private final DeleteResumeUseCase deleteResumeUseCase;

    @PostMapping
    public ResponseEntity<CreateResumeResponseDTO> createResume(@RequestBody ResumeRequestDTO request) {
        final CreateResumeResponseDTO response = createResumeUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponseDTO> getResumeById(@PathVariable UUID id) {
        final ResumeResponseDTO response = getResumeByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResumeResponseDTO> updateResume(@PathVariable UUID id, @RequestBody ResumeRequestDTO request) {
        final ResumeResponseDTO response = updateResumeUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable UUID id) {
        deleteResumeUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}