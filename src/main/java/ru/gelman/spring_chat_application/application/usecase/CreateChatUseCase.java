package ru.gelman.spring_chat_application.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gelman.spring_chat_application.application.dto.ChatDetails;
import ru.gelman.spring_chat_application.application.dto.CreateGroupChatCommand;
import ru.gelman.spring_chat_application.application.dto.CreatePersonalChatCommand;
import ru.gelman.spring_chat_application.application.mapper.ChatMapper;
import ru.gelman.spring_chat_application.application.mapper.ChatMemberMapper;
import ru.gelman.spring_chat_application.application.query.UserQueryService;
import ru.gelman.spring_chat_application.domain.model.Chat;
import ru.gelman.spring_chat_application.domain.model.ChatMember;
import ru.gelman.spring_chat_application.domain.model.ChatRole;
import ru.gelman.spring_chat_application.domain.model.User;
import ru.gelman.spring_chat_application.domain.service.DirectChatPolicy;
import ru.gelman.spring_chat_application.domain.service.GroupChatPolicy;
import ru.gelman.spring_chat_application.infrastructure.persistence.ChatMemberRepository;
import ru.gelman.spring_chat_application.infrastructure.persistence.ChatRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CreateChatUseCase {
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMapper chatMapper;
    private final ChatMemberMapper chatMemberMapper;
    private final DirectChatPolicy directChatPolicy;
    private final UserQueryService userQueryService;
    private final GroupChatPolicy groupChatPolicy;

    @Autowired
    public CreateChatUseCase(ChatRepository chatRepository, ChatMemberRepository chatMemberRepository, ChatMapper chatMapper, ChatMemberMapper chatMemberMapper, DirectChatPolicy directChatPolicy, UserQueryService service, GroupChatPolicy groupChatPolicy) {
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.chatMapper = chatMapper;
        this.chatMemberMapper = chatMemberMapper;
        this.directChatPolicy = directChatPolicy;
        this.userQueryService = service;
        this.groupChatPolicy = groupChatPolicy;
    }

    @Transactional
    public ChatDetails createPersonal(CreatePersonalChatCommand request) {
        User initiator = userQueryService.findById(request.initiatorId());
        User recipient = userQueryService.findById(request.recipientId());

        directChatPolicy.assertCanCreateChatBetween(initiator, recipient);

        Optional<Chat> chat = chatRepository.findDirectChatFor(initiator.getId(), recipient.getId());
        if (chat.isPresent()) {
            log.debug("Chat between {} and {} already exists. Return existing chat", initiator, recipient);
            Chat existing = chat.get();
            return chatMapper.convert(existing, chatMemberMapper.convert(existing.getChatMembers()));
        }

        Chat created = chatRepository.save(Chat.personal());
        List<ChatMember> members = List.of(
                chatMemberRepository.save(ChatMember.of(created, initiator, ChatRole.MEMBER)),
                chatMemberRepository.save(ChatMember.of(created, recipient, ChatRole.MEMBER))
        );

        return chatMapper.convert(created, chatMemberMapper.convert(members));
    }

    @Transactional
    public ChatDetails createGroup(CreateGroupChatCommand request) {
        groupChatPolicy.assertCanCreateGroupChatFor(request.userIds(), request.ownerId());
        Chat chat = chatRepository.save(Chat.group(request.name(), request.description()));

        List<ChatMember> members = request.userIds().stream()
                .map(userQueryService::findById)
                .map(u -> ChatMember.of(chat, u, u.getId().equals(request.ownerId()) ? ChatRole.OWNER : ChatRole.MEMBER))
                .toList();

        List<ChatMember> resultMembers = chatMemberRepository.saveAll(members);
        return chatMapper.convert(chat, chatMemberMapper.convert(resultMembers));
    }
}
