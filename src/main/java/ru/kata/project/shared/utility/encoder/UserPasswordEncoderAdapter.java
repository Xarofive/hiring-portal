package ru.kata.project.shared.utility.encoder;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.project.core.port.UserPasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserPasswordEncoderAdapter implements UserPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
