package ru.gelman.spring_chat_application.application.dto;

import java.util.List;

public record CreateGroupChatCommand(String name, String description, List<Long> userIds, Long ownerId) {
}
