package ru.kata.project.persistence.repository.jpa.intf;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.project.persistence.entity.TokenEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepositoryJpaInterface extends JpaRepository<TokenEntity, UUID> {
    Optional<TokenEntity> findByAccessToken(String accessToken);

    Optional<TokenEntity> findByRefreshToken(String refreshToken);

    List<TokenEntity> findAllByFamilyId(UUID id);

    List<TokenEntity> findAllByUserId(UUID id);
}
