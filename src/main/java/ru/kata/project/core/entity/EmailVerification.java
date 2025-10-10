package ru.kata.project.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EmailVerification {
    private UUID id;
    private UUID userId;
    private String codeHash;
    private Timestamp consumedAt;
}
