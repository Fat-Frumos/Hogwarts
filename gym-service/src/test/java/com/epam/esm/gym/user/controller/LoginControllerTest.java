package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileRequest;
import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import com.epam.esm.gym.user.security.service.JwtProvider;
import com.epam.esm.gym.user.security.service.SecurityUserDetailsService;
import com.epam.esm.gym.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link LoginController}.
 * This class is responsible for testing the functionality of the login endpoints defined in the
 * {@link LoginController}. It uses {@link WebMvcTest} to test the controller layer in isolation,
 * and it mocks the {@link UserService}, {@link BruteForceProtectionService}, and security-related services
 * such as {@link JwtProvider} and {@link SecurityUserDetailsService}.
 *
 * <p>The {@link ActiveProfiles} annotation is used to load the "test" profile, which contains the specific
 * configuration for running tests, and the {@link Import} annotation imports the {@link SecurityConfigTestMock}
 * to configure security in the test environment.</p>
 * Testing is done with {@link MockMvc} to simulate HTTP requests and responses.
 *
 * @see LoginController
 * @see WebMvcTest
 * @see MockMvc
 */
@ActiveProfiles("test")
@WebMvcTest(LoginController.class)
@Import(SecurityConfigTestMock.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private BruteForceProtectionService bruteForceProtectionService;
    @MockBean
    private SecurityUserDetailsService userDetailsService;
    private static final String base_url = "/api/login";
    private static final String username = "username";
    private static final String password = "password";

    @Test
    void testChangeLoginSuccess() throws Exception {
        ProfileRequest request = new ProfileRequest();
        request.setUsername("user");
        request.setOldPassword("oldPass");
        request.setNewPassword("newPass");
        when(userService.changePassword(any())).thenReturn(new MessageResponse("Password Changed"));
        mockMvc.perform(put(base_url + "/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"oldPassword\":\"oldPass\",\"newPassword\":\"newPass\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Password Changed\"}"));
    }

    @Test
    void loginAuthenticatedUserReturns200() throws Exception {
        MessageResponse expectedResponse = new MessageResponse("Login successful");
        when(userService.authenticate("testuser", password)).thenReturn(expectedResponse);
        mockMvc.perform(get(base_url)
                        .param(username, "testuser")
                        .param(password, password))
                .andExpect(status().isOk());
    }

    @Test
    void loginValidCredentialsReturns200() throws Exception {
        MessageResponse expectedResponse = new MessageResponse("Login successful");
        when(userService.authenticate("validUser", "validPassword")).thenReturn(expectedResponse);

        mockMvc.perform(get(base_url)
                        .param(username, "validUser")
                        .param(password, "validPassword"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Login successful\"}"));
    }

    @Test
    void loginInvalidCredentialsReturns404() throws Exception {
        when(userService.authenticate("invalidUser", "invalidPassword"))
                .thenThrow(new EntityNotFoundException("Authentication failed"));

        mockMvc.perform(get(base_url)
                        .param(username, "invalidUser")
                        .param(password, "invalidPassword"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testLoginSuccess() throws Exception {
        MessageResponse mockResponse = new MessageResponse("Success");
        when(userService.authenticate(username, password)).thenReturn(mockResponse);
        mockMvc.perform(get(base_url)
                        .param(username, username)
                        .param(password, password))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Success\"}"));
    }

    @Test
    void loginMissingUsernameReturns400() throws Exception {
        mockMvc.perform(get(base_url)
                        .param(password, "somePassword"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(
                        "{\"message\":\"Required request parameter 'username' is not present\"}"));
    }

    @Test
    void testLoginBadRequestPassword() throws Exception {
        mockMvc.perform(get(base_url)
                        .param(username, "password111"))
                .andExpect(status().isBadRequest()).andExpect(content().json(
                        "{\"message\":\"Required request parameter 'password' is not present\"}"));
        ;
    }

    private static Stream<Arguments> loginTestProvider() {
        return Stream.of(
                Arguments.of("", "validPassword", new MessageResponse("size must be between 2 and 50")),
                Arguments.of("a", "validPassword", new MessageResponse("size must be between 2 and 50")),
                Arguments.of("validUser", "", new MessageResponse("size must be between 6 and 50")),
                Arguments.of("validUser", "short", new MessageResponse("size must be between 6 and 50")),
                Arguments.of("u".repeat(51), "validPassword",
                        new MessageResponse("size must be between 2 and 50")),
                Arguments.of("validUser", "p".repeat(51),
                        new MessageResponse("size must be between 6 and 50"))
        );
    }

    /**
     * Tests that the login endpoint returns a 400 Bad Request status
     * when invalid input is provided for username or password.
     *
     * @param username     the username to be tested
     * @param password     the password to be tested
     * @param mockResponse the expected message response for the invalid input
     * @throws Exception if an error occurs during the request processing
     */
    @ParameterizedTest(name = "Test {index}: username={0}, password={1}, message={2}")
    @MethodSource("loginTestProvider")
    void loginInvalidInputReturnsBadRequest(
            String username, String password,
            MessageResponse mockResponse) throws Exception {
        mockMvc.perform(get(base_url)
                        .param(LoginControllerTest.username, username)
                        .param(LoginControllerTest.password, password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(String.format("{\"message\":\"%s\"}", mockResponse.message())));
    }
}
