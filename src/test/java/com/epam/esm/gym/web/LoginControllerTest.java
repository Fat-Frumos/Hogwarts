package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.LoginRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        loginRequest.put("username", "Harry.Potter");
        loginRequest.put("password", "password123");

        changeLogin = new HashMap<>();
        changeLogin.put("username", "Harry.Potter");
        changeLogin.put("oldPassword", "password123");
        changeLogin.put("newPassword", "newpassword123");

        ProfileResponse credentials = getProfileResponse("Harry", "Potter");
        login = Map.of(
                "username", credentials.getUsername(),
                "oldPassword", credentials.getPassword(),
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

    @Test
    void testLoginWithMissingUsername() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().password(password).build();
        String responseContent = mockMvc.perform(get("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseContent).contains("Username is required");
    }

    @Test
    void testLoginWithMissingCred() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().build();
        String responseContent = mockMvc.perform(get("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseContent).contains("Username is required");
        assertThat(responseContent).contains("Password is required");
    }

    @Test
    void testLoginWithMissingPassword() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().username("validUsername").build();
        String responseContent = mockMvc.perform(get("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseContent).contains("Password is required");
    }

    @Test
    void testLoginWithShortPassword() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().username("validUsername").password("short").build();
        String responseContent = mockMvc.perform(get("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseContent).contains("Password must be between 6 and 50 characters");
    }
}
