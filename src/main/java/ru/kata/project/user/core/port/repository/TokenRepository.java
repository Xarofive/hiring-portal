package ru.kata.project.user.core.port.repository;

import ru.kata.project.user.core.entity.Token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * TokenRepository
 * <p>
 * Порт для взаимодействия с JWT-токенами пользователя {@link Token}.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public interface TokenRepository {

    Token save(Token token);

    List<Token> saveAll(List<Token> set);

    Optional<Token> findByAccessToken(String accessToken);

    Optional<Token> findByRefreshToken(String refreshToken);

    List<Token> findAllByUserId(UUID id);

    List<Token> findAllByFamilyId(UUID familyId);
}
