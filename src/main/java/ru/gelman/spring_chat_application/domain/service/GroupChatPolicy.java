package ru.gelman.spring_chat_application.domain.service;

import org.springframework.stereotype.Component;
import ru.gelman.spring_chat_application.domain.exception.EmptyMemberListException;
import ru.gelman.spring_chat_application.domain.exception.GroupChatWithoutOwnerCreationException;

import java.util.List;

@Component
public class GroupChatPolicy {
    public void assertCanCreateGroupChatFor(List<Long> userIds, Long ownerId) {
        if (userIds == null || userIds.isEmpty()) {
            throw new EmptyMemberListException();
        }

        if (userIds.stream().noneMatch(ui -> ui.equals(ownerId))) {
            throw new GroupChatWithoutOwnerCreationException();
        }
    }
}
