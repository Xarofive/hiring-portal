package ru.kata.project.ai.adapters.stub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.ai.core.entity.ResumeId;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class StubResumeRepositoryTest {

    @InjectMocks
    private StubResumeRepository repository;

    @Test
    void givenNonZeroUuid_whenExists_thenReturnsTrue() {
        final ResumeId id = ResumeId.of(UUID.randomUUID());
        assertTrue(repository.existsById(id.getValue()));
    }

    @Test
    void givenZeroUuid_whenExists_thenReturnsFalse() {
        final ResumeId id = ResumeId.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertFalse(repository.existsById(id.getValue()));
    }
}