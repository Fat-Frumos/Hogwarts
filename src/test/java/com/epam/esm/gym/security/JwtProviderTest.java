package com.epam.esm.gym.security;

import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.UserPrincipal;
import com.epam.esm.gym.security.service.JwtProperties;
import com.epam.esm.gym.security.service.JwtProvider;
import com.epam.esm.gym.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @Mock
    private TokenService tokenService;

    @Mock
    UserPrincipal userPrincipal;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtProvider jwtProvider;

    private static final String accessToken = "accessToken";
    private static final String USERNAME = "testUser";
    private static final String TOKEN = "testToken";
    private Set<Token> tokens;
    private Token token;
    private User user;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(jwtProperties, tokenService);
        token = jwtProvider.getToken(userPrincipal, accessToken);
        Token token2 = jwtProvider.getToken(userPrincipal, TOKEN);
        tokens = Set.of(token, token2);
        user = User.builder().id(1).username(USERNAME).tokens(tokens).build();
    }

    @Test
    void testGenerateToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        String jwt = jwtProvider.generateToken(USERNAME, claims);

        assertNotNull(jwt);
        assertTrue(jwt.startsWith("eyJ"));
    }

    @Test
    void testExtractUserName() {
        String jwt = jwtProvider.generateToken(USERNAME, new HashMap<>());
        String extractedUsername = jwtProvider.extractUserName(jwt);
        assertEquals(USERNAME, extractedUsername);
    }

    @Test
    void testValidateToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        String jwt = jwtProvider.generateToken(USERNAME, claims);
        when(userPrincipal.getUsername()).thenReturn(USERNAME);
        boolean isValid = jwtProvider.validateToken(jwt, userPrincipal);
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        String jwt = jwtProvider.generateToken(USERNAME, new HashMap<>());
        boolean isExpired = jwtProvider.isTokenExpired(jwt);
        assertFalse(isExpired);
    }

    @Test
    void testRevokeAllUserTokens() {
        when(tokenService.findAllValidAccessTokenByUserId(user.getId())).thenReturn(tokens);
        jwtProvider.revokeAllUserTokens(user);
        tokens.forEach(jwt -> {
            assertTrue(jwt.isExpired());
            assertTrue(jwt.isRevoked());
        });
        verify(tokenService).saveAll(tokens);
    }

    @Test
    void testIsBearerToken() {
        String header = "Bearer token";
        boolean isBearer = jwtProvider.isBearerToken(header);
        assertTrue(isBearer);
        header = "Basic token";
        isBearer = jwtProvider.isBearerToken(header);
        assertFalse(isBearer);
    }

    @Test
    void testGenerateTokenWithUserPrincipal() {
        when(userPrincipal.getUsername()).thenReturn(USERNAME);
        when(userPrincipal.user()).thenReturn(new User());
        String jwt = jwtProvider.generateToken(userPrincipal);
        assertNotNull(jwt);
    }

    @Test
    void testFindByToken() {
        Optional<Token> tokenOptional = Optional.of(new Token());
        when(tokenService.findByAccessToken(TOKEN)).thenReturn(tokenOptional);
        Optional<Token> foundToken = jwtProvider.findByToken(TOKEN);
        assertTrue(foundToken.isPresent());
    }

    @Test
    void testSave() {
        when(tokenService.save(token)).thenReturn(token);
        Token savedToken = jwtProvider.save(token);
        assertEquals(token, savedToken);
    }

    @Test
    void testGetToken() {
        assertNotNull(token);
        assertEquals(userPrincipal.user(), token.getUser());
        assertEquals(accessToken, token.getAccessToken());
    }
}
