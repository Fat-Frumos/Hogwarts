package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.UserPrincipal;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.security.JwtProvider;
import com.epam.esm.gym.service.profile.AuthenticationUserService;
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
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    private JwtProvider provider;
    @Mock
    private PasswordEncoder encoder;
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
        when(provider.findByToken(anyString())).thenReturn(Optional.of(token));
        when(response.getWriter()).thenReturn(writer);
        service.logout(request, response);
        verify(provider).save(any(Token.class));
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
    void testFindUser_Success() {
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
}
