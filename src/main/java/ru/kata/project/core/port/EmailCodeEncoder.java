package ru.kata.project.core.port;

public interface EmailCodeEncoder {
    String encode(String token);
}
