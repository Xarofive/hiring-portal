package ru.kata.project.user.persistence.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.port.repository.TokenRepository;
import ru.kata.project.user.persistence.repository.jpa.intf.TokenRepositoryJpaInterface;
import ru.kata.project.user.shared.utility.mapper.TokenMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * TokenRepositoryJpa
 * <p>
 * Реализация JPA репозитория {@link TokenRepository}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Repository
@RequiredArgsConstructor
public class TokenRepositoryJpa implements TokenRepository {

    private final TokenRepositoryJpaInterface tokenRepository;

    @Override
    public Token save(Token token) {
        return TokenMapper.toDomain(tokenRepository.save(TokenMapper.toEntity(token)));
    }

    @Override
    public List<Token> saveAll(List<Token> list) {
        return TokenMapper.toDomainList(tokenRepository.saveAll(TokenMapper.toEntityList(list)));
    }

    @Override
    public Optional<Token> findByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken).map(TokenMapper::toDomain);
    }

    @Override
    public Optional<Token> findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken).map(TokenMapper::toDomain);
    }

    @Override
    public List<Token> findAllByUserId(UUID id) {
        return TokenMapper.toDomainList(tokenRepository.findAllByUserId(id));
    }

    @Override
    public List<Token> findAllByFamilyId(UUID familyId) {
        return TokenMapper.toDomainList(tokenRepository.findAllByFamilyId(familyId));
    }

}
