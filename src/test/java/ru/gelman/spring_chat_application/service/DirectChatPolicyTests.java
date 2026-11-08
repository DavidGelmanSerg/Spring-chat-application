package ru.gelman.spring_chat_application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.gelman.spring_chat_application.TestUserFactory;
import ru.gelman.spring_chat_application.domain.exception.SelfChatNotAllowedException;
import ru.gelman.spring_chat_application.domain.model.User;
import ru.gelman.spring_chat_application.domain.service.DirectChatPolicy;

public class DirectChatPolicyTests {
    private final DirectChatPolicy directChatService = new DirectChatPolicy();

    @Test
    public void shouldBeSuccessfulAssertionAndDoNothingTest() {
        User initiator = TestUserFactory.createTestUser(1L, "test1", "test1@gmail.com");
        User recipient = TestUserFactory.createTestUser(2L, "test2", "test2@gmail.com");

        Assertions.assertDoesNotThrow(() -> directChatService.assertCanCreateChatBetween(initiator, recipient));
    }

    @Test
    public void shouldThrowSelfChatNotAllowedExceptionTest() {
        User initiator = TestUserFactory.createTestUser(1L, "test1", "test1@gmail.com");
        Assertions.assertThrows(SelfChatNotAllowedException.class, () -> directChatService.assertCanCreateChatBetween(initiator, initiator));
    }
}
