package ru.gelman.spring_chat_application.application.dto;

public record CreatePersonalChatCommand(Long initiatorId, Long recipientId) {
}
