package ru.kata.project.user.core.event;

import java.util.UUID;

/**
 * UserRegisteredEvent
 * <p>
 * Представляет запись Kafka-события "UserRegistered".
 * Основана на AVRO-схеме "UserRegistered.avsc".
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public record UserRegisteredEvent(
        String eventId,
        String eventType,
        String schemaVersion,
        String aggregateId,
        String email,
        long occurredAt,
        String traceId,
        String source
) {
    public static UserRegisteredEvent of(UUID userId, String email) {
        return new UserRegisteredEvent(
                UUID.randomUUID().toString(),
                "UserRegistered",
                "1",
                userId.toString(),
                email,
                System.currentTimeMillis(),
                UUID.randomUUID().toString(),
                "user-module"
        );
    }
}