package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.service.UserService;
import com.epam.esm.gym.web.provider.AuthenticateArgumentsProvider;
import com.epam.esm.gym.web.provider.ChangePasswordArgumentsProvider;
import com.epam.esm.gym.web.provider.ChangePasswordValidationConstraintsArgumentsProvider;
import com.epam.esm.gym.web.provider.ValidationConstraintsAuthenticateArgumentsProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.Objects;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest extends ControllerTest {

    @MockBean
    private UserService userService;

    @ParameterizedTest
    @ArgumentsSource(AuthenticateArgumentsProvider.class)
    void testAuthenticate(String username, String password, ResponseEntity<MessageResponse> expectedResponse) throws Exception {
        when(userService.authenticate(username, password)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse.getBody())));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(ChangePasswordArgumentsProvider.class)
    void testChangePassword(ProfileRequest request, ResponseEntity<MessageResponse> expectedResponse) throws Exception {
        when(userService.changePassword(request)).thenReturn(expectedResponse);
        mockMvc.perform(put("/api/login/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse.getBody())));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(ChangePasswordValidationConstraintsArgumentsProvider.class)
    void testChangePasswordValidationConstraints(ProfileRequest request, ResponseEntity<MessageResponse> expectedResponse) throws Exception {
        when(userService.changePassword(request)).thenReturn(expectedResponse);

        String actualResponseContent = mockMvc.perform(put("/api/login/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andReturn().getResponse().getContentAsString();
        MessageResponse actualResponse = objectMapper.readValue(actualResponseContent, MessageResponse.class);

        String expectedMessages = Objects.requireNonNull(expectedResponse.getBody()).getMessage();
        String actualMessages = actualResponse.getMessage();
        Arrays.stream(expectedMessages.split(", ")).forEach(expectedMessage -> assertTrue(actualMessages.contains(expectedMessage),
                "Expected message to be present in the response but was not: " + expectedMessage));
    }

    @ParameterizedTest
    @ArgumentsSource(ValidationConstraintsAuthenticateArgumentsProvider.class)
    void testLoginValidationConstraints(String username, String password, String expectedMessage) throws Exception {
        mockMvc.perform(get("/api/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }
}
