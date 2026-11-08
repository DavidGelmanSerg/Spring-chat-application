package ru.gelman.spring_chat_application.infrastructure.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gelman.spring_chat_application.application.dto.ChatDetails;
import ru.gelman.spring_chat_application.application.dto.CreateGroupChatCommand;
import ru.gelman.spring_chat_application.application.dto.CreatePersonalChatCommand;
import ru.gelman.spring_chat_application.application.usecase.CreateChatUseCase;

@RestController
@RequestMapping("/chats")
@Slf4j
public class ChatController {
    private final CreateChatUseCase service;

    @Autowired
    public ChatController(CreateChatUseCase service) {
        this.service = service;
    }

    @PostMapping("/personal/")
    public ChatDetails postPersonal(@RequestBody CreatePersonalChatCommand request) {
        log.info("Creating new personal chat by request: {}", request);
        ChatDetails created = service.createPersonal(request);
        log.info("Created {}", created);
        return created;
    }

    @PostMapping("/group/")
    public ChatDetails postGroup(@RequestBody CreateGroupChatCommand request){
        log.info("Creating new group chat by request: {}", request);
        ChatDetails created = service.createGroup(request);
        log.info("Created {}", created);
        return created;
    }
}
