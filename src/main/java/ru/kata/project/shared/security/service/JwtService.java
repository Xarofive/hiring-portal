package ru.kata.project.shared.security.service;

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
import ru.kata.project.core.entity.Role;
import ru.kata.project.core.entity.Token;
import ru.kata.project.core.entity.User;
import ru.kata.project.core.port.repository.TokenRepository;

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
public class JwtService {

    private final TokenRepository tokenRepository;

    private final KeyService keyService;

    @Value("${security.jwt.access_token_expiration}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;

    private String generateToken(User user, long expiryMs, boolean isRefresh) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(expiryMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "hiring-portal-auth");
        claims.put("aud", "hiring-portal");
        claims.put("jti", UUID.randomUUID().toString());
        claims.put("type", isRefresh ? "refresh" : "access");

        if (!isRefresh) {
            claims.put("sid", UUID.randomUUID().toString());
            claims.put("roles", user.getRoles().stream().map(Role::getCode).toList());
            claims.put("username", user.getUsername());
            claims.put("email_verified", true);
        }

        String kid = keyService.getCurrentKid();

        return Jwts.builder()
                .header().add("kid", kid).and()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .notBefore(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(keyService.getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration, false);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, true);
    }

    public boolean isValid(String token, UserDetails user) {
        try {
            String username = extractUsername(token);
            boolean notRevoked = tokenRepository.findByAccessToken(token)
                    .map(t -> !t.isLoggedOut())
                    .orElse(false);
            return username.equals(user.getUsername()) && notRevoked && isExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isValidRefresh(String token, User user) {
        try {
            String username = extractUsername(token);
            boolean notRevoked = tokenRepository.findByRefreshToken(token)
                    .map(t -> !t.isLoggedOut())
                    .orElse(false);
            return username.equals(user.getUsername()) && notRevoked && isExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        String kid = extractKidFromHeader(token);
        RSAPublicKey key = keyService.getPublicKey(kid);

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractKidFromHeader(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new JwtException("Invalid JWT format");
        }
        String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode header = mapper.readTree(headerJson);
            return header.get("kid").asText();
        } catch (Exception e) {
            throw new JwtException("Cannot extract kid from token", e);
        }
    }

    public void saveUserToken(String accessToken, String refreshToken, User user, UUID familyId) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setFamilyId(familyId);
        token.setUserId(user.getId());
        token.setRevokedAt(null);
        tokenRepository.save(token);
    }

    public void revokeToken(Token token) {
        token.setLoggedOut(true);
        token.setRevokedAt(new Timestamp(System.currentTimeMillis()));
        tokenRepository.save(token);
    }

    public void revokeFamily(UUID familyId) {
        List<Token> familyTokens = tokenRepository.findAllByFamilyId(familyId);
        for (Token t : familyTokens) {
            t.setLoggedOut(true);
            t.setRevokedAt(new Timestamp(System.currentTimeMillis()));
        }
        tokenRepository.saveAll(familyTokens);
    }

    public void setRefreshCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    public void deleteRefreshCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

}