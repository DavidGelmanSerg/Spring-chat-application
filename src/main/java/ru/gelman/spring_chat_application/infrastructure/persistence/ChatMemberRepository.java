package ru.gelman.spring_chat_application.infrastructure.persistence;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.gelman.spring_chat_application.domain.model.ChatMember;

@Repository
public interface ChatMemberRepository extends ListCrudRepository<ChatMember, Long> {
}
