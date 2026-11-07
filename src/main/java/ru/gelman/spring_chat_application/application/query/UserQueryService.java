package ru.gelman.spring_chat_application.application.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gelman.spring_chat_application.domain.exception.UserNotFoundException;
import ru.gelman.spring_chat_application.domain.model.User;
import ru.gelman.spring_chat_application.infrastructure.persistence.UserRepository;

@Service
public class UserQueryService {
    private final UserRepository repository;

    @Autowired
    public UserQueryService(UserRepository repository) {
        this.repository = repository;
    }

    public User findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
