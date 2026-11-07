package ru.gelman.spring_chat_application.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "chat_users")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatMember {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private ChatMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @MapsId("userId")
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", updatable = false)
    @MapsId("chatId")
    @ToString.Exclude
    private Chat chat;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private ChatRole role;

    public static ChatMember of(Chat chat, User user, ChatRole role) {
        ChatMember member = new ChatMember();
        member.setId(new ChatMemberId());
        member.setChat(chat);
        member.setUser(user);
        member.setRole(role);
        return member;
    }
}
