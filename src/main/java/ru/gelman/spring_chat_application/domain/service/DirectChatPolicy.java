package ru.gelman.spring_chat_application.domain.service;

import org.springframework.stereotype.Component;
import ru.gelman.spring_chat_application.domain.exception.SelfChatNotAllowedException;
import ru.gelman.spring_chat_application.domain.model.User;

@Component
public class DirectChatPolicy {
    public void assertCanCreateChatBetween(User initiator, User recipient) {
        if (initiator.equals(recipient)) {
            throw new SelfChatNotAllowedException(initiator.getId());
        }
    }
}
