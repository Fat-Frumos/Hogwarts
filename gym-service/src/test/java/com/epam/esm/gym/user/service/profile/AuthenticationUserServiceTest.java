package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.dao.JpaUserDao;
import com.epam.esm.gym.user.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.user.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.user.dto.auth.RegisterRequest;
import com.epam.esm.gym.user.dto.auth.UserPrincipal;
import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.Token;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.security.service.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationUserServiceTest {
    @Mock
    private JpaUserDao userDao;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthenticationUserService userService;
    private static final String username = "Harry.Potter";
    private static final String password = "Password111";
    private Token token;
    private User user;

    @BeforeEach
    void setUp() {
        token = Token.builder()
                .accessToken("accessToken")
                .accessTokenTTL(Instant.now().toEpochMilli())
                .build();
        user = User.builder()
                .username(username)
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
        userService.logout(request, response);
        verify(jwtProvider).save(any(Token.class));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write("Logout successful");
    }

    @Test
    void testGetUserWithRole() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword("password");
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        User withRole = userService.getUserWithRole(request, username);
        assertNotNull(withRole);
        assertEquals(username, withRole.getUsername());
        assertEquals("encodedPassword", withRole.getPassword());
        assertEquals(RoleType.ROLE_TRAINER, withRole.getPermission());
    }

    @Test
    void testRefreshSuccessfulRefresh() {
        String refreshToken = "refreshToken";
        when(jwtProvider.isBearerToken(anyString())).thenReturn(true);
        when(jwtProvider.extractUserName(refreshToken)).thenReturn(username);
        when(jwtProvider.validateToken(anyString(), any(UserPrincipal.class))).thenReturn(true);
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtProvider.generateToken(any(UserPrincipal.class))).thenReturn("newAccessToken");
        when(jwtProvider.updateUserTokens(any(UserPrincipal.class), anyString())).thenReturn(token);

        AuthenticationResponse response =
                userService.refresh("Bearer " + refreshToken, mock(HttpServletResponse.class));

        assertNotNull(response);
        assertEquals("accessToken", Objects.requireNonNull(response).getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void testLoginUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        when(userDao.findByUsername(username)).thenReturn(Optional.empty());
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.login(request));
        assertEquals("User not found " + username, exception.getMessage());
    }
}
