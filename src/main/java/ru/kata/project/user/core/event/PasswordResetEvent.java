package ru.kata.project.user.core.event;

import java.util.UUID;

/**
 * PasswordResetEvent
 * <p>
 * Представляет запись Kafka-события "PasswordReset".
 * Основана на AVRO-схеме "PasswordReset.avsc".
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record PasswordResetEvent(
        String eventId,
        String eventType,
        String schemaVersion,
        String aggregateId,
        String email,
        long occurredAt,
        String traceId,
        String source
) {
    public static PasswordResetEvent of(UUID userId, String email) {
        return new PasswordResetEvent(
                UUID.randomUUID().toString(),
                "PasswordReset",
                "1",
                userId.toString(),
                email,
                System.currentTimeMillis(),
                UUID.randomUUID().toString(),
                "user-module"
        );
    }
}