package ru.kata.project.security.utility.encoder;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.project.user.core.port.UserPasswordEncoder;

/**
 * UserPasswordEncoderAdapter
 * <p>
 * Реализация {@link UserPasswordEncoder}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Service
@RequiredArgsConstructor
public class UserPasswordEncoderAdapter implements UserPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
