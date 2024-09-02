package com.epam.esm.gym.security;

import com.epam.esm.gym.dao.TokenDao;
import com.epam.esm.gym.domain.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCTokenServiceTest {

    @Mock
    private TokenDao dao;

    @InjectMocks
    private JDBCTokenService tokenService;
    private Token token;
    private List<Token> tokens;

    @BeforeEach
    void setUp() {
        token = new Token();
        tokens = List.of(token);
    }

    @Test
    void testFindAllValidAccessTokenByUserId() {
        when(dao.findAllValidAccessTokenByUserId(1)).thenReturn(tokens);
        List<Token> result = tokenService.findAllValidAccessTokenByUserId(1);
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
