package ru.gelman.spring_chat_application.application.dto;

public record CreateUserCommand(String name, String email) {
}
