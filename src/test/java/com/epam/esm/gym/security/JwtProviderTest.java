package com.epam.esm.gym.security;

import com.epam.esm.gym.dao.TokenDao;
import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtProviderTest {

    @Mock
    private TokenDao tokenDao;

    @Mock
    private JwtProperties jwtProperties;

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jwtProperties.getSecret()).thenReturn("mySecretKey");
        when(jwtProperties.getIssuer()).thenReturn("issuer");
        jwtProvider = new JwtProvider(jwtProperties, tokenDao);
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
        when(tokenDao.findAllValidAccessTokenByUserId(1)).thenReturn(List.of(token));
        jwtProvider.revokeAllUserTokens(user);
        verify(tokenDao).saveAll(anyList());
        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
    }

    @Test
    void testFindByToken() {
        Token token = Token.builder().build();
        when(tokenDao.findByAccessToken("token")).thenReturn(Optional.of(token));
        Optional<Token> result = jwtProvider.findByToken("token");
        assertTrue(result.isPresent());
        assertEquals(token, result.get());
    }

    @Test
    void testSave() {
        Token token = Token.builder().build();
        when(tokenDao.save(token)).thenReturn(token);
        Token result = jwtProvider.save(token);
        assertEquals(token, result);
    }

    @Test
    void testExceptionHandling() {
        when(jwtProperties.getSecret()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new JwtProvider(jwtProperties, tokenDao));
    }
}
