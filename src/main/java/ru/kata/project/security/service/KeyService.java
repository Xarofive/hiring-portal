package ru.kata.project.security.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * KeyService
 * <p>
 * Сервис загрузки приватного и публичных секретных ключей для генерации JWT-токенов.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Service
@RequiredArgsConstructor
public class KeyService {

    private final Map<String, RSAPublicKey> publicKeys = new ConcurrentHashMap<>();
    @Value("${security.jwt.private-key-path:}")
    private String privateKeyPath;
    @Value("${security.jwt.public-key-path:}")
    private String publicKeyPath;
    private volatile RSAPrivateKey currentPrivateKey;

    @Getter
    private volatile String currentKid;

    @PostConstruct
    public void init() {
        try {
            if (privateKeyPath != null && !privateKeyPath.isBlank()
                    && publicKeyPath != null && !publicKeyPath.isBlank()) {
                final RSAPrivateKey privateKey = loadPrivateKey(privateKeyPath);
                final RSAPublicKey publicKey = loadPublicKey(publicKeyPath);
                this.currentKid = "key-" + Instant.now().toEpochMilli();
                this.currentPrivateKey = privateKey;
                this.publicKeys.put(currentKid, publicKey);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize KeyService", e);
        }
    }

    public RSAPrivateKey getPrivateKey() {
        return currentPrivateKey;
    }

    public RSAPublicKey getPublicKey(String kid) {
        final RSAPublicKey key = publicKeys.get(kid);
        if (key == null) {
            throw new IllegalArgumentException("Unknown key id: " + kid);
        }
        return key;
    }

    public synchronized void rotateKeys(RSAPrivateKey privateKey, RSAPublicKey publicKey, String kid) {
        this.currentPrivateKey = privateKey;
        this.publicKeys.put(kid, publicKey);
        this.currentKid = kid;
    }

    private RSAPrivateKey loadPrivateKey(String path) throws Exception {
        try (InputStream in = new ClassPathResource(path).getInputStream()) {
            final String key = new String(in.readAllBytes())
                    .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] keyBytes = Base64.getDecoder().decode(key);
            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
        }
    }

    private RSAPublicKey loadPublicKey(String path) throws Exception {
        try (InputStream in = new ClassPathResource(path).getInputStream()) {
            final String key = new String(in.readAllBytes())
                    .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] keyBytes = Base64.getDecoder().decode(key);
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        }
    }
}