package ru.gelman.spring_chat_application.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import ru.gelman.spring_chat_application.TestUserFactory;
import ru.gelman.spring_chat_application.domain.model.User;
import ru.gelman.spring_chat_application.infrastructure.persistence.UserRepository;


@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository repository;

    @Test
    public void testExistsByEmail() {
        User user = TestUserFactory.createTestUser("testUser", "testUser@gmai.com");
        Assertions.assertFalse(repository.existsByEmail(user.getEmail()));
        repository.save(user);
        Assertions.assertTrue(repository.existsByEmail(user.getEmail()));
    }
}
