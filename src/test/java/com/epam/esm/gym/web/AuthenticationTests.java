package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.service.UserService;
import com.epam.esm.gym.web.provider.AuthenticateArgumentsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationTests extends ControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    public void testAdminAccess() throws Exception {
        mockMvc.perform(get("/api/trainees"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/trainees")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_TRAINER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testInvalidToken() throws Exception {
        mockMvc.perform(get("/api/trainees")
                        .header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @DisplayName("Successful Login")
    @WithMockUser(roles = "TRAINEE")
    @ArgumentsSource(AuthenticateArgumentsProvider.class)
    void testLogin(String username, String password, ResponseEntity<MessageResponse> expectedResponse) throws Exception {
        when(userService.authenticate(username, password)).thenReturn(expectedResponse);
        String passworde = passwordEncoder.encode("Password111");
        System.out.println(passworde);
        mockMvc.perform(get("/api/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse.getBody())));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCredentials")
    @DisplayName("Failed Login Attempts")
    void failedLoginAttempts(String username, String password) throws Exception {

        mockMvc.perform(get("/api/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isUnauthorized());
    }

    private static Stream<Arguments> provideValidCredentials() {
        return Stream.of(
                Arguments.of("Harry.Potter", "Password123"),
                Arguments.of("Hermione.Granger", "Password456")
        );
    }

    private static Stream<Arguments> provideInvalidCredentials() {
        return Stream.of(
                Arguments.of("Harry.Potter", "wrongpassword"),
                Arguments.of("Hermione.Granger", "wrongpassword")
        );
    }
}