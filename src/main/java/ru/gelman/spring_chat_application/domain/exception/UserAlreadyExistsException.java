package ru.gelman.spring_chat_application.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("User with email %s already exists".formatted(email));
    }
}
