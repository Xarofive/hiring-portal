package ru.kata.project.security.utility.encoder;

import org.springframework.stereotype.Component;
import ru.kata.project.user.core.port.EmailCodeEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * EmailCodeEncoderAdapter
 * <p>
 * Реализация {@link EmailCodeEncoder}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Component
public class EmailCodeEncoderAdapter implements EmailCodeEncoder {

    public String encode(String token) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            final byte[] digest = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
