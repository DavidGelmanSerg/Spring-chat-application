package ru.gelman.spring_chat_application.api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gelman.spring_chat_application.application.dto.CreateUserCommand;
import ru.gelman.spring_chat_application.application.dto.UserDetails;
import ru.gelman.spring_chat_application.application.usecase.CreateUserUseCase;
import ru.gelman.spring_chat_application.infrastructure.web.UserController;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CreateUserUseCase service;


    @Test
    public void postUsersTest() throws Exception {
        CreateUserCommand request = new CreateUserCommand("testUser", "testUser@gmail.com");
        UserDetails created = new UserDetails(1L, request.name(), request.email(), false);
        Mockito.when(service.create(request)).thenReturn(created);

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(created.id().intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(created.name())))
                .andExpect(jsonPath("$.email", Matchers.equalTo(created.email())))
                .andExpect(jsonPath("$.isOnline", Matchers.equalTo(created.isOnline())));
    }
}
