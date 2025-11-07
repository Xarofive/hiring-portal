package ru.kata.project.ai.application.port;

/**
 * OutboxResumeEventPublisher
 * <p>
 * Порт для работы с Outbox-механизмом.
 * Определяет контракт для публикации Kafka-событий в таблицу Outbox.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface OutboxResumeEventPublisher {
    void publish(String aggregateId,
                 String eventType,
                 Object payload,
                 String schemaVersion,
                 String source,
                 String traceId);
}