package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.auth.MessageResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.dto.auth.UserPrincipal;
import com.epam.esm.gym.security.service.JwtProvider;
import com.epam.esm.gym.service.profile.AuthenticationUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationUserServiceTest {
    @Mock
    private UserDao userRepository;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private AuthenticationManager manager;
    @InjectMocks
    private AuthenticationUserService service;
    private static final String USERNAME = "Harry.Potter";
    private Token token;
    private User user;

    @BeforeEach
    void setUp() {
        token = Token.builder()
                .accessToken("accessToken")
                .accessTokenTTL(Instant.now().toEpochMilli())
                .build();
        user = User.builder()
                .username(USERNAME)
                .password("encodedPassword")
                .permission(RoleType.ROLE_TRAINER)
                .build();

    }

    @Test
    void testLogout() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtProvider.findByToken(anyString())).thenReturn(Optional.of(token));
        when(response.getWriter()).thenReturn(writer);
        service.logout(request, response);
        verify(jwtProvider).save(any(Token.class));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write("Logout successful");
    }

    @Test
    void testGetUserWithRole() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(USERNAME);
        request.setPassword("password");
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        User withRole = service.getUserWithRole(request, USERNAME);
        assertNotNull(withRole);
        assertEquals(USERNAME, withRole.getUsername());
        assertEquals("encodedPassword", withRole.getPassword());
        assertEquals(RoleType.ROLE_TRAINER, withRole.getPermission());
    }

    @Test
    void testFindUserSuccess() {
        when(userRepository.getUserBy(USERNAME)).thenReturn(user);
        UserPrincipal securityUser = service.findUser(USERNAME);
        assertNotNull(securityUser);
        assertEquals(USERNAME, securityUser.user().getUsername());
    }

    @Test
    void testFindUserUserNotFound() {
        when(userRepository.getUserBy(USERNAME)).thenReturn(null);
        UserPrincipal securityUser = service.findUser(USERNAME);
        assertNull(securityUser.user());
    }

    @Test
    void testFindUser() {
        String username = "testUser";
        User mockUser = new User();
        when(userRepository.getUserBy(username)).thenReturn(mockUser);
        UserPrincipal result = service.findUser(username);
        assertNotNull(result);
        assertEquals(mockUser, result.user());
        verify(userRepository).getUserBy(username);
    }
    @Test
    void testLogin_UserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest(USERNAME, "password");
        when(userRepository.findByName(USERNAME)).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = service.login(request);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("User not found " + USERNAME, ((MessageResponse)
                Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void testLogin_SuccessfulAuthentication() {
        AuthenticationRequest request = new AuthenticationRequest(USERNAME, "password");
        when(userRepository.findByName(USERNAME)).thenReturn(Optional.of(user));
        Authentication authentication = mock(Authentication.class);
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtProvider.generateToken(any(UserPrincipal.class))).thenReturn("token");
        ResponseEntity<BaseResponse> response = service.login(request);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        AuthenticationResponse authResponse = (AuthenticationResponse) response.getBody();
        assertNotNull(Objects.requireNonNull(authResponse).getAccessToken());
        assertNotNull(authResponse.getRefreshToken());
    }

    @Test
    void testRefresh_InvalidToken() {
        when(jwtProvider.isBearerToken(anyString())).thenReturn(true);
        when(jwtProvider.extractUserName(anyString())).thenReturn(null);

        ResponseEntity<BaseResponse> response =
                service.refresh("Bearer token", mock(HttpServletResponse.class));

        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Invalid Jwt Authentication", ((MessageResponse)
                Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void testRefresh_SuccessfulRefresh() {
        String refreshToken = "refreshToken";
        when(jwtProvider.isBearerToken(anyString())).thenReturn(true);
        when(jwtProvider.extractUserName(refreshToken)).thenReturn(USERNAME);
        when(jwtProvider.validateToken(anyString(), any(UserPrincipal.class))).thenReturn(true);
        when(userRepository.getUserBy(USERNAME)).thenReturn(user);
        when(jwtProvider.generateToken(any(UserPrincipal.class))).thenReturn("newAccessToken");
        when(jwtProvider.updateUserTokens(any(UserPrincipal.class), anyString())).thenReturn(token);

        ResponseEntity<BaseResponse> response =
                service.refresh("Bearer " + refreshToken, mock(HttpServletResponse.class));

        assertTrue(response.getStatusCode().is2xxSuccessful());
        AuthenticationResponse authResponse = (AuthenticationResponse) response.getBody();
        assertEquals("accessToken", Objects.requireNonNull(authResponse).getAccessToken());
        assertEquals(refreshToken, authResponse.getRefreshToken());
    }
}
