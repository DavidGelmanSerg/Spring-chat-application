package ru.gelman.spring_chat_application.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type", updatable = false)
    @Enumerated(EnumType.STRING)
    private ChatType type;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chat")
    @ToString.Exclude
    private List<ChatMember> chatMembers = new ArrayList<>();

    public static Chat personal() {
        Chat chat = new Chat();
        chat.type = ChatType.DIRECT;
        chat.name = null;
        chat.description = null;
        return chat;
    }
}
