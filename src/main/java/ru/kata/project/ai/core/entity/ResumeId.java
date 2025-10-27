package ru.kata.project.ai.core.entity;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class ResumeId {
    private final UUID value;

    private ResumeId(UUID value) {
        this.value = value;
    }

    public static ResumeId of(UUID uuid) {
        Objects.requireNonNull(uuid);
        return new ResumeId(uuid);
    }

    public static ResumeId of(String str) {
        Objects.requireNonNull(str);
        return new ResumeId(UUID.fromString(str));
    }
}
