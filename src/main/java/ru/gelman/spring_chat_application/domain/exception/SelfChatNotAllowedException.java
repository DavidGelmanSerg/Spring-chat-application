package ru.gelman.spring_chat_application.domain.exception;

public class SelfChatNotAllowedException extends RuntimeException {
    public SelfChatNotAllowedException(Long id) {
        super("Attempt to create self chat for user %s".formatted(id));
    }
}
