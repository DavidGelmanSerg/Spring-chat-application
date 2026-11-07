package ru.gelman.spring_chat_application.infrastructure.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gelman.spring_chat_application.application.dto.CreateUserCommand;
import ru.gelman.spring_chat_application.application.dto.UserDetails;
import ru.gelman.spring_chat_application.application.usecase.CreateUserUseCase;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase userService;

    @Autowired
    public UserController(CreateUserUseCase userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public UserDetails postUsers(@RequestBody CreateUserCommand request) {
        log.info("Creating user from {}", request);
        UserDetails created = userService.create(request);
        log.info("Created {}", created);
        return created;
    }
}
