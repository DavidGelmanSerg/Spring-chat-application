package ru.gelman.spring_chat_application.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gelman.spring_chat_application.model.dto.CreateUserRequest;
import ru.gelman.spring_chat_application.model.dto.UserResponse;
import ru.gelman.spring_chat_application.service.UserService;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTests {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService service;


    @Test
    public void postUsersTest() throws Exception {
        CreateUserRequest request = new CreateUserRequest("testUser", "testUser@gmail.com");
        UserResponse created = new UserResponse(1L, request.name(), request.email(), false);
        Mockito.when(service.create(request)).thenReturn(created);

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                ).andExpect(jsonPath("$.id", Matchers.equalTo(created.id().intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(created.name())))
                .andExpect(jsonPath("$.email", Matchers.equalTo(created.email())))
                .andExpect(jsonPath("$.isOnline", Matchers.equalTo(created.isOnline())))
                .andExpect(status().isOk());
    }
}
