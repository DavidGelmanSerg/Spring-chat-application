package ru.gelman.spring_chat_application.application.dto;

import ru.gelman.spring_chat_application.domain.model.ChatType;

import java.time.LocalDateTime;
import java.util.List;

public record ChatDetails(Long id, String name, String description, ChatType type, LocalDateTime createdAt,
                          List<ChatMemberDetails> members) {
}
