package ru.kata.project.core.port;

public interface UserPasswordEncoder {
    String encode(String rawPassword);
}
