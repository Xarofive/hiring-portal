package ru.kata.project.shared.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * NotificationServiceConsumer
 * <p>
 * Consumer "notification-service".
 * </p>
 * <p>
 * Слушает события пользователей и вакансий.
 * </p>
 * <ul>
 *  <li> чтение из топика "user.account";</li>
 *  <li> Логи имеют отличия, исходя из прочитанного сообщения.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@Slf4j
@Service
public class NotificationServiceConsumer {

    @KafkaListener(topics = "user.account", groupId = "notification-service-group")
    public void handleUserEvent(GenericRecord record) {
        final String eventType = record.get("eventType").toString();
        final String aggregateId = record.get("aggregateId").toString();
        final String email;
        if (record.get("email") != null) {
            email = record.get("email").toString();
        } else {
            email = "unknown";
        }

        log.info("📨 [NotificationService] Received user event: type={}, aggregateId={}, email={}",
                eventType,
                aggregateId,
                email);

        switch (eventType) {
            case "UserRegistered" -> log.info("✅ [NotificationService] Send welcome notification to {}", email);
            case "PasswordReset" -> log.info("🔑 [NotificationService] Send password reset email to {}", email);
            case "EmailVerified" -> log.info("📩 [NotificationService] Confirm verification for {}", email);
            default -> log.warn("⚠️ [NotificationService] Unknown event type: {}", eventType);
        }
    }
}