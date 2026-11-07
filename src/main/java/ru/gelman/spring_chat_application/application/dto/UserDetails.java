package ru.gelman.spring_chat_application.application.dto;

public record UserDetails(Long id, String name, String email, boolean isOnline) {
}
