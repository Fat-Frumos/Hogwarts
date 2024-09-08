package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.service.UserService;
import com.epam.esm.gym.web.provider.AuthenticateArgumentsProvider;
import com.epam.esm.gym.web.provider.ChangePasswordArgumentsProvider;
import com.epam.esm.gym.web.provider.ValidationConstraintsAuthenticateArgumentsProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

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
    void testAuthenticate(String name, String password, ResponseEntity<BaseResponse> response) throws Exception {
        when(userService.authenticate(name, password)).thenReturn(response);

        mockMvc.perform(get("/api/login")
                        .param("username", name)
                        .param("password", password))
                .andExpect(status().is(response.getStatusCode().value()))
                .andExpect(content().json(objectMapper.writeValueAsString(response.getBody())));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(ChangePasswordArgumentsProvider.class)
    void testChangePassword(ProfileRequest request, ResponseEntity<BaseResponse> expectedResponse) throws Exception {
        when(userService.changePassword(request)).thenReturn(expectedResponse);
        mockMvc.perform(put("/api/login/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse.getBody())));
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
