package com.epam.esm.gym.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the global exception handling in the application.
 *
 * <p>This class tests how the application handles various exceptions such as {@link IllegalArgumentException},
 * and various authentication exceptions.
 * The tests ensure that the appropriate HTTP status codes and error messages are returned for different types
 * of exceptions.</p>
 *
 * <p>The tests use {@link MockMvc} to simulate HTTP requests and validate the responses. It is annotated with
 * {@link SpringBootTest} and {@link AutoConfigureMockMvc} to set up the Spring Boot test context and configure
 * the mock MVC environment.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void handleMethodNotSupported_ShouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/api/trainees"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message", containsString(
                        "Request method 'POST' not supported")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void handleRuntimeException_ShouldReturnInternalServerError() throws Exception {
        mockMvc.perform(get("/api/trainees")
                        .param("param", "cause_runtime_exception"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void handleSignatureExceptionShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/trainees")
                        .param("param", "exception"))
                .andExpect(status().is4xxClientError());
    }
}
