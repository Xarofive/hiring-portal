package ru.kata.project.user.shared.utility.generator;

import org.springframework.stereotype.Component;
import ru.kata.project.user.core.port.EmailCodeGenerator;

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
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(random.nextInt(9) + 1);
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
