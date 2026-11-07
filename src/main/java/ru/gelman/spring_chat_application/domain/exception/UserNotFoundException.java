package ru.gelman.spring_chat_application.domain.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with id %s not found".formatted(id));
    }
}
