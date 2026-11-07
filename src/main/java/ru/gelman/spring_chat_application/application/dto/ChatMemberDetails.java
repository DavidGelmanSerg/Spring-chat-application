package ru.gelman.spring_chat_application.application.dto;


import ru.gelman.spring_chat_application.domain.model.ChatRole;

public record ChatMemberDetails(Long userId, ChatRole role) {
}
