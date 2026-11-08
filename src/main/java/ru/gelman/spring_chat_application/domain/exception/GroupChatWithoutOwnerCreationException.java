package ru.gelman.spring_chat_application.domain.exception;

public class GroupChatWithoutOwnerCreationException extends RuntimeException {
    public GroupChatWithoutOwnerCreationException() {
        super("Cannot create group chat without owner");
    }
}