package ru.kata.project.shared.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * AnalyticsServiceConsumer
 * <p>
 * Consumer "recommendation-service".
 * </p>
 * <p>
 * Читает заданные топики для матчинга кандидатов и вакансий.
 * </p>
 * <p>
 * На данный момент матчинг не реализован, используется заглушка
 * </p>
 * <ul>
 *  <li> чтение из топиков "resume.lifecycle", "job.posting";</li>
 *  <li> Логи имеют отличия, исходя из прочитанного сообщения.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@Slf4j
@Service
public class RecommendationServiceConsumer {

    @KafkaListener(
            topics = {"resume.lifecycle", "job.posting"},
            groupId = "recommendation-service-group"
    )
    public void handleRecommendationEvent(GenericRecord record,
                                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            final String eventType = getSafe(record, "eventType");
            final String eventId = getSafe(record, "eventId");
            final String traceId = getSafe(record, "traceId");

            log.info("🤖 [RecommendationService] Received event from topic='{}' → type='{}', eventId='{}', traceId='{}'",
                    topic, eventType, eventId, traceId);

            switch (eventType) {
                case "ResumeGenerated" -> handleResumeGenerated(record);
                case "ResumeAnalyzed" -> handleResumeAnalyzed(record);

                case "JobPublished" -> handleJobPublished(record);
                case "ApplicationStatusChanged" -> handleApplicationStatusChanged(record);

                default -> log.warn("⚠️ [RecommendationService] Unknown event type '{}' from topic '{}'",
                        eventType,
                        topic);
            }
        } catch (Exception e) {
            log.error("❌ [RecommendationService] Failed to process message from topic='{}': {}",
                    topic,
                    e.getMessage(),
                    e);
        }
    }

    private void handleResumeGenerated(GenericRecord record) {
        final String title = getSafe(record, "title");
        final String aggregateId = getSafe(record, "aggregateId");
        log.info("📄 ResumeGenerated → New resume '{}' (aggregateId={}) added to recommendation index",
                title,
                aggregateId);
    }

    private void handleResumeAnalyzed(GenericRecord record) {
        final String aggregateId = getSafe(record, "aggregateId");
        log.info("🧠 ResumeAnalyzed → Updating recommendation vectors for resume aggregateId='{}'", aggregateId);
    }

    private void handleJobPublished(GenericRecord record) {
        final String aggregateId = getSafe(record, "aggregateId");
        log.info("💼 JobPublished → Adding job aggregateId='{}' to recommendation pool", aggregateId);
    }

    private void handleApplicationStatusChanged(GenericRecord record) {
        final String aggregateId = getSafe(record, "aggregateId");
        log.info("📨 ApplicationStatusChanged → Updating candidate-job match status for application='{}'",
                aggregateId);
    }

    private String getSafe(GenericRecord record, String field) {
        final Object value = record.get(field);
        if (value != null) {
            return value.toString();
        }
        return "N/A";
    }
}