package ru.gelman.spring_chat_application.domain.exception;

public class EmptyMemberListException extends RuntimeException {
    public EmptyMemberListException() {
        super("Cannot create chat with empty member list");
    }
}
