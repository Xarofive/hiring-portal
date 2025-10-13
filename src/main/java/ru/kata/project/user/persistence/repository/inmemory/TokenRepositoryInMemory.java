package ru.kata.project.user.persistence.repository.inmemory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.port.repository.TokenRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * TokenRepositoryInMemory
 * <p>
 * Реализация in-memory репозитория {@link TokenRepository}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Repository
@Primary
public class TokenRepositoryInMemory implements TokenRepository {

    private final Map<UUID, Token> tokens = new ConcurrentHashMap<>();

    @Override
    public Token save(Token token) {
        if (token.getId() == null) {
            token.setId(UUID.randomUUID());
        }
        tokens.put(token.getId(), token);
        return token;
    }

    @Override
    public List<Token> saveAll(List<Token> tokenList) {
        tokenList.forEach(this::save);
        return tokenList;
    }

    @Override
    public Optional<Token> findByAccessToken(String accessToken) {
        return tokens.values().stream()
                .filter(t -> accessToken.equals(t.getAccessToken()))
                .findFirst();
    }

    @Override
    public Optional<Token> findByRefreshToken(String refreshToken) {
        return tokens.values().stream()
                .filter(t -> refreshToken.equals(t.getRefreshToken()))
                .findFirst();
    }

    @Override
    public List<Token> findAllByUserId(UUID id) {
        return tokens.values().stream()
                .filter(t -> t.getUserId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<Token> findAllByFamilyId(UUID familyId) {
        return tokens.values().stream()
                .filter(t -> familyId.equals(t.getFamilyId()))
                .collect(Collectors.toList());
    }

    public void revokeToken(UUID tokenId) {
        Token token = tokens.get(tokenId);
        if (token != null) {
            token.setRevokedAt(new Timestamp(System.currentTimeMillis()));
        }
    }
}
