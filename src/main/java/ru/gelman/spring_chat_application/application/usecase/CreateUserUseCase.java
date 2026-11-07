package ru.gelman.spring_chat_application.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gelman.spring_chat_application.application.dto.CreateUserCommand;
import ru.gelman.spring_chat_application.application.dto.UserDetails;
import ru.gelman.spring_chat_application.application.mapper.UserMapper;
import ru.gelman.spring_chat_application.domain.exception.UserAlreadyExistsException;
import ru.gelman.spring_chat_application.domain.model.User;
import ru.gelman.spring_chat_application.infrastructure.persistence.UserRepository;

@Service
@Slf4j
public class CreateUserUseCase {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Autowired
    public CreateUserUseCase(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public UserDetails create(CreateUserCommand request) {
        if (repository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }
        User user = repository.save(mapper.convert(request));
        log.debug("Saved {}", user);
        return mapper.convert(user);
    }
}
