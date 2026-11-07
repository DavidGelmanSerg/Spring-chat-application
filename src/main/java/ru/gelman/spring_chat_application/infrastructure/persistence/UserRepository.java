package ru.gelman.spring_chat_application.infrastructure.persistence;

import org.springframework.data.repository.ListCrudRepository;
import ru.gelman.spring_chat_application.domain.model.User;

public interface UserRepository extends ListCrudRepository<User, Long> {
    boolean existsByEmail(String email);
}
