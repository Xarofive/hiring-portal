package ru.kata.project.shared.kafka.outbox.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kata.project.ai.application.port.OutboxResumeEventPublisher;
import ru.kata.project.shared.kafka.outbox.entity.OutboxEvent;
import ru.kata.project.shared.kafka.outbox.repository.OutboxRepository;
import ru.kata.project.user.core.port.service.OutboxUserEventPublisher;

import java.time.Instant;
import java.util.UUID;

/**
 * OutboxPublisherImpl
 * <p>
 * Реализация портов для работы с Outbox-механизмом.
 * Сохраняет события в базе данных "outbox_event" для атомарной записи этих событий.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Service
@RequiredArgsConstructor
public class OutboxPublisherImpl implements OutboxUserEventPublisher, OutboxResumeEventPublisher {

    private final OutboxRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(String aggregateId,
                        String eventType,
                        Object payload,
                        String schemaVersion,
                        String source,
                        String traceId) {
        try {
            final String jsonPayload = objectMapper.writeValueAsString(payload);
            final String finalTraceId;
            if (traceId != null) {
                finalTraceId = traceId;
            } else {
                finalTraceId = UUID.randomUUID().toString();
            }
            final OutboxEvent event = OutboxEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .aggregateId(aggregateId)
                    .eventType(eventType)
                    .schemaVersion(schemaVersion)
                    .traceId(finalTraceId)
                    .occurredAt(Instant.now())
                    .source(source)
                    .payload(jsonPayload)
                    .status(OutboxEvent.Status.NEW)
                    .retryCount(0)
                    .build();

            repository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize and save OutboxEvent", e);
        }
    }
}