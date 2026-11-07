package ru.gelman.spring_chat_application;

import ru.gelman.spring_chat_application.domain.model.User;

public class TestUserFactory {
    public static User createTestUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    public static User createTestUser(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
