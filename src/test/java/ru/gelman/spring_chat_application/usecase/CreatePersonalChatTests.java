package ru.gelman.spring_chat_application.usecase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.gelman.spring_chat_application.TestUserFactory;
import ru.gelman.spring_chat_application.application.dto.ChatDetails;
import ru.gelman.spring_chat_application.application.dto.CreatePersonalChatCommand;
import ru.gelman.spring_chat_application.application.mapper.ChatMapper;
import ru.gelman.spring_chat_application.application.mapper.ChatMemberMapper;
import ru.gelman.spring_chat_application.application.query.UserQueryService;
import ru.gelman.spring_chat_application.application.usecase.CreateChatUseCase;
import ru.gelman.spring_chat_application.domain.model.*;
import ru.gelman.spring_chat_application.domain.service.DirectChatService;
import ru.gelman.spring_chat_application.infrastructure.persistence.ChatMemberRepository;
import ru.gelman.spring_chat_application.infrastructure.persistence.ChatRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CreatePersonalChatTests {
    private static User initiator;
    private static User recipient;
    private static CreatePersonalChatCommand request;
    private static Chat testDirectChat;
    @Autowired
    private CreateChatUseCase createChatUseCase;
    @MockitoBean
    private ChatRepository chatRepository;
    @MockitoBean
    private ChatMemberRepository chatMemberRepository;
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private ChatMemberMapper chatMemberMapper;
    @MockitoBean
    private DirectChatService directChatPolicy;
    @MockitoBean
    private UserQueryService userQueryService;

    @BeforeAll
    public static void prepareTestUsers() {
        initiator = TestUserFactory.createTestUser(1L, "test1", "test1@gmail.com");
        recipient = TestUserFactory.createTestUser(2L, "test2", "test2@gmail.com");
        request = new CreatePersonalChatCommand(initiator.getId(), recipient.getId());
        testDirectChat = Chat.personal();
        testDirectChat.setId(1L);
    }

    @Test
    public void shouldCreatePersonalChat() {
        ChatMember initiatorMember = ChatMember.of(testDirectChat, initiator, ChatRole.MEMBER);
        ChatMember initiatorMemberCreated = ChatMember.of(testDirectChat, initiator, ChatRole.MEMBER);
        initiatorMemberCreated.setId(new ChatMemberId(initiator.getId(), testDirectChat.getId()));
        ChatMember recipientMember = ChatMember.of(testDirectChat, recipient, ChatRole.MEMBER);
        ChatMember recipientMemberCreated = ChatMember.of(testDirectChat, recipient, ChatRole.MEMBER);
        recipientMemberCreated.setId(new ChatMemberId(recipient.getId(), testDirectChat.getId()));

        Mockito.when(userQueryService.findById(request.initiatorId())).thenReturn(initiator);
        Mockito.when(userQueryService.findById(request.recipientId())).thenReturn(recipient);
        Mockito.doNothing().when(directChatPolicy).assertCanCreateChatBetween(initiator, recipient);
        Mockito.when(chatRepository.findDirectChatFor(initiator.getId(), recipient.getId())).thenReturn(Optional.empty());
        Mockito.when(chatRepository.save(Chat.personal())).thenReturn(testDirectChat);
        Mockito.when(chatMemberRepository.save(initiatorMember)).thenReturn(initiatorMemberCreated);
        Mockito.when(chatMemberRepository.save(recipientMember)).thenReturn(recipientMemberCreated);

        ChatDetails chatDetails = createChatUseCase.createPersonal(request);
        Assertions.assertEquals(chatDetails.id(), testDirectChat.getId());
        Assertions.assertNull(chatDetails.name());
        Assertions.assertNull(chatDetails.description());
        Assertions.assertEquals(chatDetails.members().size(), 2);
        Assertions.assertEquals(chatDetails.type(), ChatType.DIRECT);
    }

    @Test
    public void shouldReturnExistingChat() {
        ChatMember initiatorMember = ChatMember.of(testDirectChat, initiator, ChatRole.MEMBER);
        initiatorMember.setId(new ChatMemberId(initiator.getId(), testDirectChat.getId()));
        ChatMember recipientMember = ChatMember.of(testDirectChat, recipient, ChatRole.MEMBER);
        recipientMember.setId(new ChatMemberId(recipient.getId(), testDirectChat.getId()));
        testDirectChat.setChatMembers(List.of(initiatorMember, recipientMember));

        Mockito.when(userQueryService.findById(request.initiatorId())).thenReturn(initiator);
        Mockito.when(userQueryService.findById(request.recipientId())).thenReturn(recipient);
        Mockito.doNothing().when(directChatPolicy).assertCanCreateChatBetween(initiator, recipient);
        Mockito.when(chatRepository.findDirectChatFor(initiator.getId(), recipient.getId())).thenReturn(Optional.of(testDirectChat));

        ChatDetails chatDetails = createChatUseCase.createPersonal(request);
        Assertions.assertEquals(chatDetails.id(), testDirectChat.getId());
        Assertions.assertNull(chatDetails.name());
        Assertions.assertNull(chatDetails.description());
        Assertions.assertEquals(chatDetails.members().size(), 2);
        Assertions.assertEquals(chatDetails.type(), ChatType.DIRECT);
    }
}
