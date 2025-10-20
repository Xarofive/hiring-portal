package ru.kata.project.security.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.kata.project.user.core.entity.Role;
import ru.kata.project.user.core.entity.Token;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.port.repository.TokenRepository;
import ru.kata.project.user.core.port.service.JwtService;

import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceAdapter implements JwtService {

    private final TokenRepository tokenRepository;

    private final KeyService keyService;

    @Value("${security.jwt.access_token_expiration}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;

    private String generateToken(User user, long expiryMs, boolean isRefresh) {
        final UUID jti = UUID.randomUUID();

        final Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "hiring-portal-auth");
        claims.put("aud", "hiring-portal");
        claims.put("jti", jti.toString());
        if (isRefresh) {
            claims.put("type", "refresh");
        } else {
            claims.put("type", "access");
        }

        if (!isRefresh) {
            claims.put("sid", UUID.randomUUID().toString());
            claims.put("roles", user.getRoles().stream().map(Role::getCode).toList());
            claims.put("username", user.getUsername());
            claims.put("email_verified", true);
        }

        final String kid = keyService.getCurrentKid();

        final Instant now = Instant.now();
        final Instant exp = now.plusMillis(expiryMs);

        return Jwts.builder()
                .id(jti.toString())
                .header().add("kid", kid).and()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .notBefore(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(keyService.getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    @Override
    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration, false);
    }

    @Override
    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, true);
    }

    @Override
    public boolean isValid(String token, UserDetails user) {
        try {
            final String username = extractUsername(token);
            final boolean notRevoked = tokenRepository.findByAccessToken(token)
                    .map(t -> !t.isLoggedOut())
                    .orElse(false);
            return username.equals(user.getUsername()) && notRevoked && isExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public boolean isValidRefresh(String token, User user) {
        try {
            final String username = extractUsername(token);
            final boolean notRevoked = tokenRepository.findByRefreshToken(token)
                    .map(t -> !t.isLoggedOut())
                    .orElse(false);
            return username.equals(user.getUsername()) && notRevoked && isExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public boolean isExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    @Override
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public String extractJti(String token) {
        return extractAllClaims(token).getId();
    }

    private Claims extractAllClaims(String token) {
        final String kid = extractKidFromHeader(token);
        final RSAPublicKey key = keyService.getPublicKey(kid);

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String extractKidFromHeader(String token) {
        final String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new JwtException("Invalid JWT format");
        }
        final String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode header = mapper.readTree(headerJson);
            return header.get("kid").asText();
        } catch (Exception e) {
            throw new JwtException("Cannot extract kid from token", e);
        }
    }

    @Override
    public void saveUserToken(String accessToken, String refreshToken, User user, UUID familyId) {
        final Token token = new Token();
        token.setId(UUID.fromString(extractJti(accessToken)));
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setFamilyId(familyId);
        token.setUserId(user.getId());
        token.setRevokedAt(null);
        tokenRepository.save(token);
    }

    @Override
    public void revokeToken(Token token) {
        token.setLoggedOut(true);
        token.setRevokedAt(new Timestamp(System.currentTimeMillis()));
        tokenRepository.save(token);
    }

    @Override
    public void revokeFamily(UUID familyId) {
        final List<Token> familyTokens = tokenRepository.findAllByFamilyId(familyId);
        for (Token t : familyTokens) {
            t.setLoggedOut(true);
            t.setRevokedAt(new Timestamp(System.currentTimeMillis()));
        }
        tokenRepository.saveAll(familyTokens);
    }

    @Override
    public void setRefreshCookie(String refreshToken, HttpServletResponse response) {
        final Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    @Override
    public void deleteRefreshCookie(HttpServletResponse response) {
        final Cookie cookie = new Cookie("refresh_token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    @Override
    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}