package ru.kata.project.core.port.repository;

import ru.kata.project.core.entity.Token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository {

    Token save(Token token);

    List<Token> saveAll(List<Token> set);

    Optional<Token> findByAccessToken(String accessToken);

    Optional<Token> findByRefreshToken(String refreshToken);

    List<Token> findAllByUserId(UUID id);

    List<Token> findAllByFamilyId(UUID familyId);
}
