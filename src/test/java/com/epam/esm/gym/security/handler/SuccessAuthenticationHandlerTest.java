package com.epam.esm.gym.security.handler;

import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.UserPrincipal;
import com.epam.esm.gym.service.profile.AuthenticationUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuccessAuthenticationHandlerTest {

    @Mock
    private AuthenticationUserService service;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private SuccessAuthenticationHandler successAuthenticationHandler;

    @Mock
    private PrintWriter writer;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testOnAuthenticationSuccess() throws IOException {
        UserPrincipal userPrincipal = mock(UserPrincipal.class);
        AuthenticationResponse authResponse = mock(AuthenticationResponse.class);

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(service.getAuthenticationResponse(userPrincipal)).thenReturn(authResponse);
        when(objectMapper.writeValueAsString(authResponse)).thenReturn("{\"token\":\"mockToken\"}");

        successAuthenticationHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write("{\"token\":\"mockToken\"}");
    }

    @Test
    void testOnAuthenticationSuccesswithJsonProcessingException() throws IOException {
        UserPrincipal userPrincipal = mock(UserPrincipal.class);
        AuthenticationResponse authResponse = mock(AuthenticationResponse.class);

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(service.getAuthenticationResponse(userPrincipal)).thenReturn(authResponse);
        when(objectMapper.writeValueAsString(authResponse)).thenThrow(new JsonProcessingException("mock exception") {});

        successAuthenticationHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write("{}");
    }
}
