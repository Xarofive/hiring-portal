package ru.kata.project.user.persistence.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.kata.project.user.core.entity.AuthAudit;
import ru.kata.project.user.core.port.repository.AuthAuditRepository;
import ru.kata.project.user.persistence.repository.jpa.intf.AuthAuditRepositoryJpaInterface;
import ru.kata.project.user.utility.mapper.AuthAuditMapper;

import java.util.List;

/**
 * AuthAuditRepositoryJpa
 * <p>
 * Реализация JPA репозитория {@link AuthAuditRepository}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Repository
@RequiredArgsConstructor
public class AuthAuditRepositoryJpa implements AuthAuditRepository {

    private final AuthAuditRepositoryJpaInterface authAuditRepository;

    @Override
    public AuthAudit save(AuthAudit audit) {
        return AuthAuditMapper.toDomain(authAuditRepository.save(AuthAuditMapper.toEntity(audit)));
    }

    @Override
    public List<AuthAudit> findAll() {
        return AuthAuditMapper.toDomainList(authAuditRepository.findAll());
    }
}
