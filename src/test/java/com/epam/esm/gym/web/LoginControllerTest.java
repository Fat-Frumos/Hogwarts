package com.epam.esm.gym.web;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest extends ControllerTest {

    private Map<String, String> loginRequest;
    private Map<String, String> changeLogin;
    private Map<String, String> login;

    @BeforeEach
    void setUp() {
        loginRequest = new HashMap<>();
        loginRequest.put("username", "harry.potter");
        loginRequest.put("password", "password123");

        changeLogin = new HashMap<>();
        changeLogin.put("username", "harry.potter");
        changeLogin.put("oldPassword", "password123");
        changeLogin.put("newPassword", "newpassword123");

        var credentials = generateUserCredentials("Harry", "Potter");
        login = Map.of(
                "username", credentials.get("username"),
                "oldPassword", credentials.get("password"),
                "newPassword", "newPassword123");
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeLogin() throws Exception {
        mockMvc.perform(put("/api/login/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeLogin)))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeLoginUserCredentials() throws Exception {
        mockMvc.perform(put("/api/login/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk());
    }
}
