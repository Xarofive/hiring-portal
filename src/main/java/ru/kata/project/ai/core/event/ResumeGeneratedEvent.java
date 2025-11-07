package ru.kata.project.ai.core.event;

import java.util.UUID;

/**
 * ResumeGeneratedEvent
 * <p>
 * Представляет запись Kafka-события "ResumeGenerated".
 * Основана на AVRO-схеме "ResumeGenerated.avsc".
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record ResumeGeneratedEvent(
        String eventId,
        String eventType,
        String schemaVersion,
        String aggregateId,
        String title,
        long occurredAt,
        String traceId,
        String source
) {
    public static ResumeGeneratedEvent of(String title) {
        final UUID id = UUID.randomUUID();
        return new ResumeGeneratedEvent(
                id.toString(),
                "ResumeGenerated",
                "1",
                id.toString(),
                title,
                System.currentTimeMillis(),
                UUID.randomUUID().toString(),
                "ai-resume-module"
        );
    }
}