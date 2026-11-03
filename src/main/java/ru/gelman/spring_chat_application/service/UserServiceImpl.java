package ru.gelman.spring_chat_application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gelman.spring_chat_application.exception.AlreadyExistsException;
import ru.gelman.spring_chat_application.mapper.UserMapper;
import ru.gelman.spring_chat_application.model.dto.CreateUserRequest;
import ru.gelman.spring_chat_application.model.dto.UserResponse;
import ru.gelman.spring_chat_application.model.entity.User;
import ru.gelman.spring_chat_application.repository.UserRepository;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new AlreadyExistsException(String.format("User with email %s already exists", request.email()));
        }
        User user = repository.save(mapper.convert(request));
        log.debug("Saved {}", user);
        return mapper.convert(user);
    }
}
