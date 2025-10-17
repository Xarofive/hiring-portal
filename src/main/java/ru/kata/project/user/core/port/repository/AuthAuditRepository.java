package ru.kata.project.user.core.port.repository;

import ru.kata.project.user.core.entity.AuthAudit;

import java.util.List;

/**
 * AuthAuditRepository
 * <p>
 * Порт для взаимодействия с сущностями аудита {@link AuthAudit}.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface AuthAuditRepository {

    AuthAudit save(AuthAudit audit);

    List<AuthAudit> findAll();
}
