package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.service.profile.AuthenticationUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest extends ControllerTest {

    @Mock
    private AuthenticationUserService authenticationUserService;

    @InjectMocks
    private AuthenticationController controller;

    @Test
    void testSignupSuccess() {
        RegisterRequest request = new RegisterRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        when(authenticationUserService.signup(any(RegisterRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = controller.signup(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationUserService).signup(request);
    }

    @Test
    void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        when(authenticationUserService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = controller.authenticate(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationUserService).authenticate(request);
    }

    @Test
    void testRefreshTokensSuccess() {
        String authHeader = "Bearer token";
        AuthenticationResponse response = new AuthenticationResponse();
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        when(authenticationUserService.refresh(eq(authHeader), any(HttpServletResponse.class)))
                .thenReturn(response);
        ResponseEntity<AuthenticationResponse> result = controller.refreshTokens(authHeader, servletResponse);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationUserService).refresh(authHeader, servletResponse);
    }

    @Test
    void testLoginSuccess() {
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        when(authenticationUserService.login(any(AuthenticationRequest.class))).thenReturn(response);
        ResponseEntity<AuthenticationResponse> result = controller.login(loginRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationUserService).login(loginRequest);
    }

    @Test
    void testLogoutSuccess() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        ResponseEntity<Object> result = controller.logout(servletRequest, servletResponse);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(authenticationUserService).logout(servletRequest, servletResponse);
    }
}