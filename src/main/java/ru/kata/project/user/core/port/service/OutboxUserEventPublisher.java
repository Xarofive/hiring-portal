package ru.kata.project.user.core.port.service;

/**
 * OutboxUserEventPublisher
 * <p>
 * Порт для работы с Outbox-механизмом.
 * Определяет контракт для публикации Kafka-событий в таблицу Outbox.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface OutboxUserEventPublisher {

    void publish(String aggregateId,
                 String eventType,
                 Object payload,
                 String schemaVersion,
                 String source,
                 String traceId);
}