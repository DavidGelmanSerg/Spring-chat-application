package ru.gelman.spring_chat_application.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.gelman.spring_chat_application.model.entity.User;

public interface UserRepository extends ListCrudRepository<User, Long> {
    boolean existsByEmail(String email);
}
