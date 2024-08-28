package com.epam.esm.gym.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("provideValidCredentials")
    @DisplayName("Successful Login")
    void successfulLogin(String username, String password) throws Exception {
        mockMvc.perform(get("/api/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk());
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
                Arguments.of("Harry.Potter", "correctpassword"),
                Arguments.of("Hermione.Granger", "correctpassword")
        );
    }

    private static Stream<Arguments> provideInvalidCredentials() {
        return Stream.of(
                Arguments.of("Harry.Potter", "wrongpassword"),
                Arguments.of("Hermione.Granger", "wrongpassword")
        );
    }
}