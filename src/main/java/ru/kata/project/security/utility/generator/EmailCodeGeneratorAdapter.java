package ru.kata.project.security.utility.generator;

import org.springframework.stereotype.Component;
import ru.kata.project.user.core.port.utility.EmailCodeGenerator;

import java.util.Random;

/**
 * EmailCodeGeneratorAdapter
 * <p>
 * Реализация {@link EmailCodeGenerator}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Component
public class EmailCodeGeneratorAdapter implements EmailCodeGenerator {
    public String generate() {
        final Random random = new Random();
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(random.nextInt(9) + 1);
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
