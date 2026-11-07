package ru.gelman.spring_chat_application.usecase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.gelman.spring_chat_application.TestUserFactory;
import ru.gelman.spring_chat_application.application.dto.CreateUserCommand;
import ru.gelman.spring_chat_application.application.dto.UserDetails;
import ru.gelman.spring_chat_application.application.mapper.UserMapper;
import ru.gelman.spring_chat_application.application.usecase.CreateUserUseCase;
import ru.gelman.spring_chat_application.domain.exception.UserAlreadyExistsException;
import ru.gelman.spring_chat_application.domain.model.User;
import ru.gelman.spring_chat_application.infrastructure.persistence.UserRepository;

@SpringBootTest
public class CreateUserTests {

    @Autowired
    private CreateUserUseCase service;
    @Autowired
    private UserMapper mapper;
    @MockitoBean
    private UserRepository repository;

    @Test
    void shouldCreateUser() {
        CreateUserCommand request = new CreateUserCommand("testUser", "testUser@gmail.com");
        User testUser = mapper.convert(request);
        User created = TestUserFactory.createTestUser(1L, testUser.getName(), testUser.getEmail());
        Mockito.when(repository.existsByEmail(testUser.getEmail())).thenReturn(false);
        Mockito.when(repository.save(testUser)).thenReturn(created);

        UserDetails user = service.create(request);
        assert user != null;
        Assertions.assertEquals(user.id(), created.getId());
        Assertions.assertEquals(user.name(), created.getName());
        Assertions.assertEquals(user.email(), created.getEmail());
    }

    @Test
    void shouldThrowAlreadyExistsException() {
        CreateUserCommand request = new CreateUserCommand("testUser", "testUser@gmail.com");
        User testUser = TestUserFactory.createTestUser(request.name(), request.email());
        Mockito.when(repository.existsByEmail(testUser.getEmail())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> service.create(request));
    }
}
