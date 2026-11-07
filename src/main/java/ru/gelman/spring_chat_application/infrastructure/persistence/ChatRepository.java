package ru.gelman.spring_chat_application.infrastructure.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.gelman.spring_chat_application.domain.model.Chat;

import java.util.Optional;

public interface ChatRepository extends ListCrudRepository<Chat, Long> {
    @Query(value = """
               SELECT c FROM Chat c
               WHERE c.type = ru.gelman.spring_chat_application.domain.model.chat.ChatType.DIRECT
                 AND EXISTS (SELECT 1 FROM ChatMember m1 WHERE m1.chat.id = c.id AND m1.user.id = :initiatorId)
                 AND EXISTS (SELECT 1 FROM ChatMember m2 WHERE m2.chat.id = c.id AND m2.user.id = :recipientId)
            """
    )
    Optional<Chat> findDirectChatFor(@Param("initiatorId") Long initiatorId, @Param("recipientId") Long recipientId);
}
