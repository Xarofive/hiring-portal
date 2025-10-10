package ru.kata.project.shared.utility.generator;

import org.springframework.stereotype.Component;
import ru.kata.project.core.port.EmailCodeGenerator;

import java.util.Random;

@Component
public class EmailCodeGeneratorAdapter implements EmailCodeGenerator {
    public String generate() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(random.nextInt(9) + 1);
        for (int i = 0; i < 9; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
