package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.user.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.user.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.user.dto.auth.BaseResponse;
import com.epam.esm.gym.user.dto.auth.RegisterRequest;
import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import com.epam.esm.gym.user.security.service.JwtProvider;
import com.epam.esm.gym.user.security.service.SecurityUserDetailsService;
import com.epam.esm.gym.user.service.AuthenticationService;
import com.epam.esm.gym.user.service.profile.AuthenticationUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AuthenticationControllerTest is a test class for the AuthenticationController.
 * It contains tests to verify the behavior of the authentication endpoints,
 * ensuring that they return the expected HTTP status codes and responses.
 */
@ActiveProfiles("test")
@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfigTestMock.class)
class AuthenticationControllerTest {

    private static final String testUser = "testUser";

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService service;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private BruteForceProtectionService bruteForceProtectionService;
    @MockBean
    private SecurityUserDetailsService userDetailsService;
    @Mock
    private AuthenticationUserService authenticationUserService;
    @InjectMocks
    private AuthenticationController controller;
    private static final String base_url = "/api/auth";
    private final AuthenticationResponse response = new AuthenticationResponse();
    private final ResponseEntity<AuthenticationResponse> authResponse = ResponseEntity.ok(response);
    private final AuthenticationResponse expectedResponse = new AuthenticationResponse(
            testUser, "accessToken", "refreshToken",
            Timestamp.valueOf(LocalDateTime.now().plusDays(1)));


    @Test
    void testSignupSuccess() {
        RegisterRequest request = new RegisterRequest();
        when(authenticationUserService.signup(any(RegisterRequest.class))).thenReturn(authResponse);
        ResponseEntity<AuthenticationResponse> result = controller.signupUser(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result);
        assertEquals(response, result.getBody());
        verify(authenticationUserService).signup(request);
    }

    @Test
    void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest();
        when(authenticationUserService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);
        ResponseEntity<AuthenticationResponse> result = controller.authenticateUser(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationUserService).authenticate(request);
    }

    @Test
    void testRefreshTokensSuccess() {
        String authHeader = "Bearer token";
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        when(authenticationUserService.refresh(eq(authHeader), any(HttpServletResponse.class)))
                .thenReturn(authResponse.getBody());
        ResponseEntity<AuthenticationResponse> result = controller.refreshTokens(authHeader, servletResponse);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result);
        assertEquals(authResponse, result);
        verify(authenticationUserService).refresh(authHeader, servletResponse);
    }

    @Test
    void testLoginSuccess() {
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        when(authenticationUserService.login(any(AuthenticationRequest.class))).thenReturn(authResponse.getBody());
        ResponseEntity<AuthenticationResponse> result = controller.loginUser(loginRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(authResponse, result);
        verify(authenticationUserService).login(loginRequest);
    }

    @Test
    void testLogoutSuccess() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        ResponseEntity<BaseResponse> result = controller.logout(servletRequest, servletResponse);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
        verify(authenticationUserService).logout(servletRequest, servletResponse);
    }

    @Test
    void loginUserValidCredentialsReturnsOk() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(testUser, "password");
        when(service.login(any(AuthenticationRequest.class))).thenReturn(expectedResponse);
        mockMvc.perform(post(base_url + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(expectedResponse)));
    }

    @Test
    void signupUserValidRequestReturnsCreated() throws Exception {
        setAuthentication();
        RegisterRequest request = new RegisterRequest(
                testUser, "Test", "User", "password");
        when(service.signup(any(RegisterRequest.class))).thenReturn(
                ResponseEntity.status(HttpStatus.CREATED).body(expectedResponse));
        mockMvc.perform(post(base_url + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(expectedResponse)));
    }

    @Test
    void authenticateUserValidCredentialsReturnsOk() throws Exception {
        setAuthentication();
        AuthenticationRequest request = new AuthenticationRequest(testUser, "password");
        when(service.authenticate(any(AuthenticationRequest.class))).thenReturn(expectedResponse);
        mockMvc.perform(post(base_url + "/token/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(expectedResponse)));
    }

    @Test
    void refreshTokensValidTokenReturnsOk() throws Exception {
        String refreshToken = "refreshToken";
        setAuthentication();
        when(service.refresh(any(String.class), any(HttpServletResponse.class))).thenReturn(expectedResponse);
        mockMvc.perform(post(base_url + "/token/refresh")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(expectedResponse)));
    }

    @Test
    void logoutUserSuccessfullyLogsOutReturnsOk() throws Exception {
        setAuthentication();
        mockMvc.perform(post(base_url + "/logout"))
                .andExpect(status().isOk());
        verify(service).logout(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @Test
    void signupUserUnauthorizedAccessReturnsForbidden() throws Exception {
        mockMvc.perform(post(base_url + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new RegisterRequest(
                                testUser, "Test", "User", "password"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void authenticateUserUnauthorizedAccessReturnsForbidden() throws Exception {
        mockMvc.perform(post(base_url + "/token/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new AuthenticationRequest(testUser, "password"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void refreshTokensUnauthorizedAccessReturnsForbidden() throws Exception {
        mockMvc.perform(post(base_url + "/token/refresh"))
                .andExpect(status().isForbidden());
    }

    @Test
    void logoutUnauthorizedAccessReturnsForbidden() throws Exception {
        mockMvc.perform(post(base_url + "/logout"))
                .andExpect(status().isForbidden());
    }

    /**
     * Sets the authentication context for the given username.
     * This method creates an authentication token and sets it in the
     * security context, simulating an authenticated user.
     *
     */
    private void setAuthentication() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Converts an object to its JSON string representation.
     * This method uses the ObjectMapper to serialize the given object.
     *
     * @param obj the object to be converted to JSON
     * @return a JSON string representation of the object
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
