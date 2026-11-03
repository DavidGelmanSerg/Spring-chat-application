package ru.gelman.spring_chat_application.service;

import ru.gelman.spring_chat_application.model.dto.CreateUserRequest;
import ru.gelman.spring_chat_application.model.dto.UserResponse;

public interface UserService {
    UserResponse create(CreateUserRequest request);
}
