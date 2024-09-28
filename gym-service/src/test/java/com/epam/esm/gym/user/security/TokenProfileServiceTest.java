package com.epam.esm.gym.user.security;

import com.epam.esm.gym.user.dao.JpaTokenDao;
import com.epam.esm.gym.user.entity.Token;
import com.epam.esm.gym.user.service.profile.TokenProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link TokenProfileService} class.
 * This class uses Mockito for mocking dependencies and testing the behavior of
 * the TokenProfileService methods.
 * Tests are designed to verify correct interactions and outputs based on various input scenarios.
 */
@ExtendWith(MockitoExtension.class)
class TokenProfileServiceTest {

    @Mock
    private JpaTokenDao dao;

    @InjectMocks
    private TokenProfileService tokenService;
    private Token token;
    private Set<Token> tokens;

    @BeforeEach
    void setUp() {
        token = new Token();
        tokens = Set.of(token);
    }

    @Test
    void testFindAllValidAccessTokenByUserId() {
        when(dao.findAllValidAccessTokenByUserId(1)).thenReturn(tokens);
        Set<Token> result = tokenService.findAllValidAccessTokenByUserId(1);
        assertEquals(tokens, result);
        verify(dao).findAllValidAccessTokenByUserId(1);
    }

    @Test
    void testFindByAccessToken() {
        when(dao.findByAccessToken("jwt")).thenReturn(Optional.of(token));
        Optional<Token> result = tokenService.findByAccessToken("jwt");
        assertEquals(Optional.of(token), result);
        verify(dao).findByAccessToken("jwt");
    }

    @Test
    void testSave() {
        when(dao.save(token)).thenReturn(token);
        Token result = tokenService.save(token);
        assertEquals(token, result);
        verify(dao).save(token);
    }
}
