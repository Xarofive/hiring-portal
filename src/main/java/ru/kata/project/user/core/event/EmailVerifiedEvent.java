package ru.kata.project.user.core.event;

import java.util.UUID;

/**
 * EmailVerifiedEvent
 * <p>
 * Представляет запись Kafka-события "EmailVerified".
 * Основана на AVRO-схеме "EmailVerified.avsc".
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record EmailVerifiedEvent(
        String eventId,
        String eventType,
        String schemaVersion,
        String aggregateId,
        String email,
        long occurredAt,
        String traceId,
        String source
) {
    public static EmailVerifiedEvent of(UUID userId, String email) {
        return new EmailVerifiedEvent(
                UUID.randomUUID().toString(),
                "EmailVerified",
                "1",
                userId.toString(),
                email,
                System.currentTimeMillis(),
                UUID.randomUUID().toString(),
                "user-module"
        );
    }
}