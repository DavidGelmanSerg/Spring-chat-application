package ru.gelman.spring_chat_application.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gelman.spring_chat_application.application.dto.ChatDetails;
import ru.gelman.spring_chat_application.application.dto.ChatMemberDetails;
import ru.gelman.spring_chat_application.application.dto.CreateGroupChatCommand;
import ru.gelman.spring_chat_application.application.dto.CreatePersonalChatCommand;
import ru.gelman.spring_chat_application.application.usecase.CreateChatUseCase;
import ru.gelman.spring_chat_application.domain.model.ChatRole;
import ru.gelman.spring_chat_application.domain.model.ChatType;
import ru.gelman.spring_chat_application.infrastructure.web.ChatController;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTests {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CreateChatUseCase createChatUseCase;

    @Test
    public void postChatsPersonalTest() throws Exception {
        CreatePersonalChatCommand request = new CreatePersonalChatCommand(1L, 2L);
        ChatDetails created = new ChatDetails(
                1L,
                null,
                null,
                ChatType.DIRECT,
                LocalDateTime.now(),
                List.of(
                        new ChatMemberDetails(1L, ChatRole.MEMBER),
                        new ChatMemberDetails(2L, ChatRole.MEMBER)
                )
        );
        Mockito.when(createChatUseCase.createPersonal(request)).thenReturn(created);

        mockMvc.perform(post("/chats/personal/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(created.id().intValue())))
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.type").value(ChatType.DIRECT.name()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.members", hasSize(2)))
                .andExpect(jsonPath("$.members[*].role").exists())
                .andExpect(jsonPath("$.members[*].userId").exists());
    }

    @Test
    public void postChatsGroupTest() throws Exception {
        CreateGroupChatCommand request = new CreateGroupChatCommand("testChat", "testDescription", List.of(1L, 2L, 3L), 1L);
        ChatDetails created = new ChatDetails(
                1L,
                request.name(),
                request.description(),
                ChatType.GROUP,
                LocalDateTime.now(),
                List.of(
                        new ChatMemberDetails(1L, ChatRole.OWNER),
                        new ChatMemberDetails(2L, ChatRole.MEMBER),
                        new ChatMemberDetails(3L, ChatRole.MEMBER)
                )
        );
        Mockito.when(createChatUseCase.createGroup(request)).thenReturn(created);

        mockMvc.perform(post("/chats/group/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(created.id().intValue())))
                .andExpect(jsonPath("$.name").value(created.name()))
                .andExpect(jsonPath("$.description").value(created.description()))
                .andExpect(jsonPath("$.type").value(ChatType.GROUP.name()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.members", hasSize(created.members().size())))
                .andExpect(jsonPath("$.members[*].role").exists())
                .andExpect(jsonPath("$.members[*].userId").exists());
    }
}
