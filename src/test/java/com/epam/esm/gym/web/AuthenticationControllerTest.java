package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.service.AuthenticationService;
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
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void testSignupSuccess() {
        RegisterRequest request = new RegisterRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        when(authenticationService.signup(any(RegisterRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.signup(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationService).signup(request);
    }

    @Test
    void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.authenticate(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationService).authenticate(request);
    }

    @Test
    void testRefreshTokensSuccess() {
        String authHeader = "Bearer token";
        AuthenticationResponse response = new AuthenticationResponse();
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        when(authenticationService.refresh(eq(authHeader), any(HttpServletResponse.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.refreshTokens(authHeader, servletResponse);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationService).refresh(authHeader, servletResponse);
    }

    @Test
    void testLoginSuccess() {
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        when(authenticationService.login(any(AuthenticationRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.login(loginRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationService).login(loginRequest);
    }

    @Test
    void testLogoutSuccess() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        ResponseEntity<Object> result = authenticationController.logout(servletRequest, servletResponse);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(authenticationService).logout(servletRequest, servletResponse);
    }
}