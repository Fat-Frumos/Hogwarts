package com.epam.esm.gym.user.security.handler;

import com.epam.esm.gym.user.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.user.dto.auth.UserPrincipal;
import com.epam.esm.gym.user.entity.Token;
import com.epam.esm.gym.user.security.service.JwtProvider;
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

import java.io.PrintWriter;
import java.sql.Timestamp;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuccessAuthenticationHandlerTest {
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
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private UserPrincipal userPrincipal;
    @Mock
    private Token token;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        when(response.getWriter()).thenReturn(writer);
    }

    @SneakyThrows
    @Test
    void testOnAuthenticationSuccess() {
        String jwtToken = "test-jwt-token";
        long accessTokenTTL = 3600L;
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + accessTokenTTL * 1000);
        AuthenticationResponse authResponse = new AuthenticationResponse(
                "test-user", jwtToken, "test-refresh-token", expiresAt);
        String authResponseJson = "{\"username\":\"test-user\",\"access_token\":\"test-jwt-token\"," +
                "\"refresh_token\":\"test-refresh-token\",\"expires_at\":\"" + expiresAt + "\"}";

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(jwtProvider.generateToken(userPrincipal)).thenReturn(jwtToken);
        when(jwtProvider.updateUserTokens(userPrincipal, jwtToken)).thenReturn(token);
        when(token.getAccessTokenTTL()).thenReturn(accessTokenTTL);
        when(jwtProvider.getAuthenticationResponse(userPrincipal, jwtToken, accessTokenTTL)).thenReturn(authResponse);
        when(objectMapper.writeValueAsString(authResponse)).thenReturn(authResponseJson);

        successAuthenticationHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(authResponseJson);
        verify(jwtProvider).generateToken(userPrincipal);
        verify(jwtProvider).updateUserTokens(userPrincipal, jwtToken);
        verify(jwtProvider).getAuthenticationResponse(userPrincipal, jwtToken, accessTokenTTL);
        verify(objectMapper).writeValueAsString(authResponse);
    }

    @SneakyThrows
    @Test
    void testOnAuthenticationSuccessJsonProcessingException() {
        String jwtToken = "test-jwt-token";
        long accessTokenTTL = 3600L;
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + accessTokenTTL * 1000);
        AuthenticationResponse authResponse = new AuthenticationResponse(
                "test-user", jwtToken, "test-refresh-token", expiresAt);

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(jwtProvider.generateToken(userPrincipal)).thenReturn(jwtToken);
        when(jwtProvider.updateUserTokens(userPrincipal, jwtToken)).thenReturn(token);
        when(token.getAccessTokenTTL()).thenReturn(accessTokenTTL);
        when(jwtProvider.getAuthenticationResponse(userPrincipal, jwtToken, accessTokenTTL)).thenReturn(authResponse);
        when(objectMapper.writeValueAsString(authResponse)).thenThrow(new JsonProcessingException("error") {});

        successAuthenticationHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write("{}");
    }
}
