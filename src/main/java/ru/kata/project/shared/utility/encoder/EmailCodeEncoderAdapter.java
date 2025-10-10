package ru.kata.project.shared.utility.encoder;

import org.springframework.stereotype.Component;
import ru.kata.project.core.port.EmailCodeEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class EmailCodeEncoderAdapter implements EmailCodeEncoder {

    public String encode(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
