package ru.gelman.spring_chat_application.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import ru.gelman.spring_chat_application.TestUserFactory;
import ru.gelman.spring_chat_application.domain.model.Chat;
import ru.gelman.spring_chat_application.domain.model.ChatMember;
import ru.gelman.spring_chat_application.domain.model.ChatRole;
import ru.gelman.spring_chat_application.domain.model.User;
import ru.gelman.spring_chat_application.infrastructure.persistence.ChatRepository;

import java.util.Optional;


@DataJpaTest
public class ChatRepositoryTests {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void shouldFindDirectChatBetweenUsers() {
        User initiator = em.persist(TestUserFactory.createTestUser("test1", "test1@gmail.com"));
        User recipient = em.persist(TestUserFactory.createTestUser("test2", "test2@gmail.com"));
        Chat chat = em.persist(Chat.personal());
        em.persist(ChatMember.of(chat, initiator, ChatRole.MEMBER));
        em.persist(ChatMember.of(chat, recipient, ChatRole.MEMBER));

        Optional<Chat> found = chatRepository.findDirectChatFor(initiator.getId(), recipient.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getId(), chat.getId());
    }

    @Test
    public void shouldNotFindDirectChatBetweenUsers() {
        User initiator = em.persist(TestUserFactory.createTestUser("test1", "test1@gmail.com"));
        User recipient = em.persist(TestUserFactory.createTestUser("test2", "test2@gmail.com"));

        Optional<Chat> found = chatRepository.findDirectChatFor(initiator.getId(), recipient.getId());
        Assertions.assertFalse(found.isPresent());
    }
}
