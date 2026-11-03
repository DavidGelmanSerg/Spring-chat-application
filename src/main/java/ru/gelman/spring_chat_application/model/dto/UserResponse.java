package ru.gelman.spring_chat_application.model.dto;

public record UserResponse(Long id, String name, String email, boolean isOnline) {
}
