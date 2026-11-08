package ru.gelman.spring_chat_application.usecase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.gelman.spring_chat_application.TestUserFactory;
import ru.gelman.spring_chat_application.application.dto.ChatDetails;
import ru.gelman.spring_chat_application.application.dto.CreateGroupChatCommand;
import ru.gelman.spring_chat_application.application.mapper.ChatMapper;
import ru.gelman.spring_chat_application.application.mapper.ChatMemberMapper;
import ru.gelman.spring_chat_application.application.query.UserQueryService;
import ru.gelman.spring_chat_application.application.usecase.CreateChatUseCase;
import ru.gelman.spring_chat_application.domain.model.*;
import ru.gelman.spring_chat_application.domain.service.GroupChatPolicy;
import ru.gelman.spring_chat_application.infrastructure.persistence.ChatMemberRepository;
import ru.gelman.spring_chat_application.infrastructure.persistence.ChatRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@SpringBootTest
public class CreateGroupChatTests {
    private static List<User> users;
    private static CreateGroupChatCommand request;
    private static Chat testGroupChat;
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
    private GroupChatPolicy groupChatPolicy;
    @MockitoBean
    private UserQueryService userQueryService;

    @BeforeAll
    public static void prepareTestUsers() {
        users = List.of(
                TestUserFactory.createTestUser(1L, "test1", "test1@gmail.com"),
                TestUserFactory.createTestUser(2L, "test2", "test2@gmail.com"),
                TestUserFactory.createTestUser(3L, "test3", "test3@gmail.com")
        );
        User owner = users.get(0);
        List<Long> userIds = users.stream().map(User::getId).toList();
        request = new CreateGroupChatCommand("testChat", "testDescription", userIds, owner.getId());
        testGroupChat = Chat.group(request.name(), request.description());
        testGroupChat.setId(1L);
    }

    @Test
    public void shouldCreateGroupChat() {
        List<ChatMember> members = users.stream()
                .map(u -> {
                    ChatRole role = u.getId().equals(request.ownerId()) ? ChatRole.OWNER : ChatRole.MEMBER;
                    return ChatMember.of(testGroupChat, u, role);
                })
                .toList();

        List<ChatMember> savedMembers = members.stream()
                .peek(cm -> cm.setId(new ChatMemberId(cm.getUser().getId(), cm.getChat().getId())))
                .toList();

        Mockito.doNothing().when(groupChatPolicy).assertCanCreateGroupChatFor(request.userIds(), request.ownerId());
        Mockito.when(chatRepository.save(Chat.group(request.name(), request.description()))).thenReturn(testGroupChat);
        users.forEach(u -> Mockito.when(userQueryService.findById(u.getId())).thenReturn(u));
        Mockito.when(chatMemberRepository.saveAll(anyList())).thenReturn(savedMembers);

        ChatDetails chatDetails = createChatUseCase.createGroup(request);
        Assertions.assertEquals(chatDetails.id(), testGroupChat.getId());
        Assertions.assertEquals(chatDetails.name(), testGroupChat.getName());
        Assertions.assertEquals(chatDetails.description(), testGroupChat.getDescription());
        Assertions.assertEquals(chatDetails.members().size(), savedMembers.size());
        Assertions.assertEquals(chatDetails.type(), ChatType.GROUP);

        InOrder algOrder = Mockito.inOrder(groupChatPolicy, chatRepository, chatMemberRepository);
        algOrder.verify(groupChatPolicy).assertCanCreateGroupChatFor(anyList(), any(Long.class));
        algOrder.verify(chatRepository).save(any(Chat.class));
        algOrder.verify(chatMemberRepository).saveAll(anyList());
    }
}
