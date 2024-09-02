package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.Role;
import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.SecurityUser;
import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager manager;
    @Mock
    private UserDao userRepository;
    @Mock
    private JwtProvider provider;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthenticationService service;
    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private final String username = "Harry.Potter";
    private SecurityUser securityUser;
    private Token token;
    private User user;

    @BeforeEach
    void setUp() {
        authenticationRequest = AuthenticationRequest.builder()
                .username(username)
                .password("password123")
                .build();

        token = Token.builder()
                .accessToken("accessToken")
                .accessTokenTTL(Instant.now().toEpochMilli())
                .build();

        user = User.builder().username(username).build();

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("Harry.Potter");
        registerRequest.setPassword("password123");

        user = User.builder()
                .username("Harry.Potter")
                .password("encodedPassword")
                .role(Role.builder().permission(RoleType.ROLE_TRAINER).build())
                .build();

        securityUser = SecurityUser.builder().user(user).build();
    }

    @Test
    void testLogin() {
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(provider.generateToken(any(SecurityUser.class))).thenReturn("accessToken");
        when(provider.updateUserTokens(any(SecurityUser.class), anyString())).thenReturn(token);
        AuthenticationResponse response = service.login(authenticationRequest);
        assertEquals(username, response.getUsername());
        assertEquals("accessToken", response.getAccessToken());
        verify(manager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticate() {
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername("Harry.Potter")).thenReturn(Optional.of(user));
        doNothing().when(provider).revokeAllUserTokens(any(User.class));
        when(provider.generateToken(any(SecurityUser.class))).thenReturn("accessToken");
        when(provider.updateUserTokens(any(SecurityUser.class), anyString())).thenReturn(token);
        AuthenticationResponse response = service.authenticate(authenticationRequest);
        assertEquals(username, response.getUsername());
        assertEquals("accessToken", response.getAccessToken());
        verify(provider).revokeAllUserTokens(any(User.class));
    }

    @Test
    void testRefresh() {
        String authorizationHeader = "Bearer refreshToken";
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(provider.isBearerToken(anyString())).thenReturn(true);
        when(provider.getUsername(anyString())).thenReturn(username);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(provider.isTokenValid(anyString(), any(SecurityUser.class))).thenReturn(true);
        when(provider.generateToken(any(SecurityUser.class))).thenReturn("newAccessToken");
        when(provider.updateUserTokens(any(SecurityUser.class), anyString())).thenReturn(token);
        AuthenticationResponse responseObj = service.refresh(authorizationHeader, response);
        assertEquals(username, responseObj.getUsername());
        assertEquals("accessToken", responseObj.getAccessToken());
        verify(provider).generateToken(any(SecurityUser.class));
    }

    @Test
    void testLogout() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(provider.findByToken(anyString())).thenReturn(Optional.of(token));
        when(response.getWriter()).thenReturn(writer);
        service.logout(request, response);
        verify(provider).save(any(Token.class));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write("Logout successful");
    }
}
