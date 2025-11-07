package ru.kata.project.ai.core.event;

import java.util.UUID;

/**
 * ResumeAnalyzedEvent
 * <p>
 * Представляет запись Kafka-события "ResumeAnalyzed".
 * Основана на AVRO-схеме "ResumeAnalyzed.avsc".
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record ResumeAnalyzedEvent(
        String eventId,
        String eventType,
        String schemaVersion,
        String aggregateId,
        long occurredAt,
        String traceId,
        String source
) {
    public static ResumeAnalyzedEvent of(UUID resumeId) {
        return new ResumeAnalyzedEvent(
                UUID.randomUUID().toString(),
                "ResumeAnalyzed",
                "1",
                resumeId.toString(),
                System.currentTimeMillis(),
                UUID.randomUUID().toString(),
                "ai-resume-module"
        );
    }
}