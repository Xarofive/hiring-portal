package ru.kata.project.shared.kafka.outbox.scheduler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kata.project.shared.kafka.outbox.entity.OutboxEvent;
import ru.kata.project.shared.kafka.outbox.repository.OutboxRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * OutboxScheduler
 * <p>
 * Глобальный планировщик для реализации Outbox-механизма.
 * </p>
 * <p>
 * Используется для публикации Kafka-событий из Outbox-БД в Kafka-топики.
 * </p>
 * <p>
 * Пауза между отправками - fixed-delay = 5000.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {

    private static final Map<String, List<String>> SCHEMA_MAP = Map.of(
            "user", List.of("UserRegistered", "PasswordReset", "EmailVerified"),
            "resume", List.of("ResumeGenerated", "ResumeAnalyzed"),
            "job", List.of("JobPublished", "ApplicationStatusChanged"),
            "notification", List.of("NotificationRequested")
    );

    private static final Map<String, String> TOPIC_MAP = Map.of(
            "user-module", "user.account",
            "ai-resume-module", "resume.lifecycle",
            "job-module", "job.posting",
            "notification-module", "notification.outbox"
    );

    private final OutboxRepository repository;
    private final KafkaTemplate<String, GenericRecord> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        final List<OutboxEvent> events = repository.findTop50ByStatusOrderByOccurredAtAsc(OutboxEvent.Status.NEW);

        for (OutboxEvent event : events) {
            try {
                final String topic = resolveTopic(event.getSource());
                final Schema schema = loadSchema(event.getEventType());
                final GenericRecord record = jsonToGenericRecord(event.getPayload(), schema);

                final ProducerRecord<String, GenericRecord> message = new ProducerRecord<>(topic, event.getAggregateId(), record);
                addHeaders(message, event);

                kafkaTemplate.send(message);

                event.setStatus(OutboxEvent.Status.SENT);
                event.setPublishedAt(Instant.now());
                repository.save(event);

                log.info("✅ Published event '{}' to topic '{}'", event.getEventType(), topic);
            } catch (Exception e) {
                handleFailure(event, e);
            }
        }
    }

    private void addHeaders(ProducerRecord<String, GenericRecord> message, OutboxEvent event) {
        message.headers()
                .add(new RecordHeader("event-type", event.getEventType().getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader("schema-version", event.getSchemaVersion().getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader("trace-id", event.getTraceId().getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader("occurred-at", event.getOccurredAt().toString().getBytes(StandardCharsets.UTF_8)));
    }

    private void handleFailure(OutboxEvent event, Exception e) {
        log.error("❌ Failed to publish event {}: {}", event.getEventId(), e.getMessage());

        final int attempt = event.getRetryCount();
        if (attempt < 5) {
            event.setRetryCount(attempt + 1);
            event.setStatus(OutboxEvent.Status.NEW);
            repository.save(event);

            final long delayMillis = (long) Math.pow(2, attempt) * 1000L;
            log.warn("🔁 Retrying event {} (attempt #{}, delay {} ms)", event.getEventId(), attempt + 1, delayMillis);
        } else {
            sendToDlq(event, resolveTopic(event.getSource()));
        }
    }

    private void sendToDlq(OutboxEvent event, String mainTopic) {
        final String dlqTopic = mainTopic + ".dlq";
        try {
            final Schema schema = loadSchema("DeadLetterEvent");
            final GenericRecord record = new GenericData.Record(schema);
            record.put("originalEventType", event.getEventType());
            record.put("payload", event.getPayload());
            record.put("errorMessage", "Failed to publish after retries");
            record.put("failedAt", Instant.now().toEpochMilli());

            final ProducerRecord<String, GenericRecord> message = new ProducerRecord<>(dlqTopic, event.getAggregateId(), record);
            kafkaTemplate.send(message);

            event.setStatus(OutboxEvent.Status.FAILED);
            repository.save(event);

            log.warn("❌ Event {} moved to DLQ '{}'", event.getEventId(), dlqTopic);
        } catch (Exception ex) {
            log.error("💣 Failed to send event {} to DLQ: {}", event.getEventId(), ex.getMessage());
        }
    }

    private Schema loadSchema(String eventType) {
        final String namespace = findNamespace(eventType);
        final String schemaPath = String.format("avro/%s/%s.avsc", namespace, eventType);
        try (InputStream in = new ClassPathResource(schemaPath).getInputStream()) {
            return new Schema.Parser().parse(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load Avro schema: " + schemaPath, e);
        }
    }

    private String findNamespace(String eventType) {
        return SCHEMA_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().contains(eventType))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("system");
    }

    private GenericRecord jsonToGenericRecord(String json, Schema schema) throws IOException {
        final JsonNode node = objectMapper.readTree(json);
        final GenericRecord record = new GenericData.Record(schema);

        for (Schema.Field field : schema.getFields()) {
            final JsonNode value = node.get(field.name());
            if (value == null || value.isNull()) {
                continue;
            }

            switch (field.schema().getType()) {
                case STRING -> record.put(field.name(), value.asText());
                case INT -> record.put(field.name(), value.asInt());
                case LONG -> record.put(field.name(), value.asLong());
                case BOOLEAN -> record.put(field.name(), value.asBoolean());
                case DOUBLE -> record.put(field.name(), value.asDouble());
                default -> record.put(field.name(), value.asText());
            }
        }
        return record;
    }

    private String resolveTopic(String source) {
        return TOPIC_MAP.getOrDefault(source, "dead.letter.queue");
    }
}