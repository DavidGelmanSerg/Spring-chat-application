package ru.gelman.spring_chat_application.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import ru.gelman.spring_chat_application.model.entity.User;


@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository repository;

    @Test
    public void testExistsByEmail() {
        User user = new User(null, "testUser", "testUser@gmail.com", false, null);
        Assertions.assertFalse(repository.existsByEmail(user.getEmail()));
        repository.save(user);
        Assertions.assertTrue(repository.existsByEmail(user.getEmail()));
    }
}
