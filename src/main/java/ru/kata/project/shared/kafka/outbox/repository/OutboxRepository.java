package ru.kata.project.shared.kafka.outbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.project.shared.kafka.outbox.entity.OutboxEvent;

import java.util.List;
import java.util.UUID;

/**
 * OutboxRepository
 * <p>
 * Интерфейс для работы с базой данных, содержащей объекты {@link OutboxEvent}.
 * </p>
 * <p>
 * Используется Spring {@link JpaRepository}.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findTop50ByStatusOrderByOccurredAtAsc(OutboxEvent.Status status);
}