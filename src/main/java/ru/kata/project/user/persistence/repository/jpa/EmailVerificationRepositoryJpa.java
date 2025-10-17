package ru.kata.project.user.persistence.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.kata.project.user.core.entity.EmailVerification;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.persistence.repository.jpa.intf.EmailVerificationRepositoryJpaInterface;
import ru.kata.project.user.utility.mapper.EmailVerificationMapper;

import java.util.Optional;

/**
 * EmailVerificationRepositoryJpa
 * <p>
 * Реализация JPA репозитория {@link EmailVerificationRepository}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryJpa implements EmailVerificationRepository {

    private final EmailVerificationRepositoryJpaInterface emailVerificationRepository;

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return EmailVerificationMapper.toDomain(emailVerificationRepository.save(
                EmailVerificationMapper.toEntity(emailVerification)));
    }

    @Override
    public Optional<EmailVerification> findByCodeHash(String codeHash) {
        return emailVerificationRepository.findByCodeHash(codeHash).map(EmailVerificationMapper::toDomain);
    }
}
