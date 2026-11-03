package ru.gelman.spring_chat_application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.gelman.spring_chat_application.exception.AlreadyExistsException;
import ru.gelman.spring_chat_application.mapper.UserMapper;
import ru.gelman.spring_chat_application.model.dto.CreateUserRequest;
import ru.gelman.spring_chat_application.model.dto.UserResponse;
import ru.gelman.spring_chat_application.model.entity.User;
import ru.gelman.spring_chat_application.repository.UserRepository;

import java.time.LocalDateTime;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService service;
    @Autowired
    private UserMapper mapper;
    @MockitoBean
    private UserRepository repository;

    @Test
    void shouldCreateUser() {
        CreateUserRequest request = new CreateUserRequest("testUser", "testUser@gmail.com");
        User testUser = mapper.convert(request);
        User created = new User(1L, testUser.getName(), testUser.getEmail(), false, LocalDateTime.now());
        Mockito.when(repository.existsByEmail(testUser.getEmail())).thenReturn(false);
        Mockito.when(repository.save(testUser)).thenReturn(created);

        UserResponse user = service.create(request);
        assert user != null;
        Assertions.assertEquals(user.id(), created.getId());
        Assertions.assertEquals(user.name(), created.getName());
        Assertions.assertEquals(user.email(), created.getEmail());
    }

    @Test
    void shouldThrowAlreadyExistsException() {
        CreateUserRequest request = new CreateUserRequest("testUser", "testUser@gmail.com");
        User testUser = new User(null, request.name(), request.email(), false, LocalDateTime.now());
        Mockito.when(repository.existsByEmail(testUser.getEmail())).thenReturn(true);

        Assertions.assertThrows(AlreadyExistsException.class, () -> service.create(request));
    }
}
