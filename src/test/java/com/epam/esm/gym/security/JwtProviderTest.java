package com.epam.esm.gym.security;

import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @Mock
    private TokenService service;

    @Mock
    private JwtProperties jwtProperties;

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecret()).thenReturn("mySecretKey");
        jwtProvider = new JwtProvider(jwtProperties, service);
    }

    @Test
    void testIsBearerToken() {
        boolean result = jwtProvider.isBearerToken("Bearer token");
        assertTrue(result);
    }

    @Test
    void testRevokeAllUserTokens() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1);
        Token token = Token.builder().build();
        when(service.findAllValidAccessTokenByUserId(1)).thenReturn(List.of(token));
        jwtProvider.revokeAllUserTokens(user);
        verify(service).saveAll(anyList());
        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
    }

    @Test
    void testFindByToken() {
        Token token = Token.builder().build();
        when(service.findByAccessToken("token")).thenReturn(Optional.of(token));
        Optional<Token> result = jwtProvider.findByToken("token");
        assertTrue(result.isPresent());
        assertEquals(token, result.get());
    }

    @Test
    void testSave() {
        Token token = Token.builder().build();
        when(service.save(token)).thenReturn(token);
        Token result = jwtProvider.save(token);
        assertEquals(token, result);
    }

    @Test
    void testExceptionHandling() {
        when(jwtProperties.getSecret()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new JwtProvider(jwtProperties, service));
    }
}
