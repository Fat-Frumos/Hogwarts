package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.domain.Token;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCTokenDaoTest {

    @Mock
    private Query<Token> query;
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private JDBCTokenDao jdbcTokenDao;

    private Token token;

    @BeforeEach
    void setUp() {
        token = new Token();
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        jdbcTokenDao = new JDBCTokenDao(sessionFactory);
    }

    @Test
    @DisplayName("Should return token by username")
    void testFindByUsername() {
        when(session.createQuery(anyString(), eq(Token.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(token));
        Optional<Token> result = jdbcTokenDao.findByUsername("testUser");
        assertTrue(result.isPresent());
        assertEquals(token, result.get());
    }

    @Test
    @DisplayName("Should return empty optional if no token found by username")
    void testFindByUsernameNotFound() {
        when(session.createQuery(anyString(), eq(Token.class)))
                .thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.empty());
        Optional<Token> result = jdbcTokenDao.findByUsername("testUser");
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return all valid tokens by user ID")
    void testFindAllValidAccessTokenByUserId() {
        Set<Token> tokenSet = new HashSet<>();
        tokenSet.add(token);
        when(session.createQuery(anyString(), eq(Token.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>(tokenSet));
        Set<Token> result = jdbcTokenDao.findAllValidAccessTokenByUserId(1);
        assertEquals(tokenSet, result);
    }

    @Test
    @DisplayName("Should return empty set if no valid tokens found by user ID")
    void testFindAllValidAccessTokenByUserIdNotFound() {
        when(session.createQuery(anyString(), eq(Token.class)))
                .thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        Set<Token> result = jdbcTokenDao.findAllValidAccessTokenByUserId(1);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return token by access token string")
    void testFindByAccessToken() {
        when(session.createQuery(anyString(), eq(Token.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(token));
        Optional<Token> result = jdbcTokenDao.findByAccessToken("testAccessToken");
        assertTrue(result.isPresent());
        assertEquals(token, result.get());
    }

    @Test
    @DisplayName("Should return empty optional if no token found by access token string")
    void testFindByAccessTokenNotFound() {
        when(session.createQuery(anyString(), eq(Token.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.empty());
        Optional<Token> result = jdbcTokenDao.findByAccessToken("testAccessToken");
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should save all tokens")
    void testSaveAll() {
        Token token1 = new Token();
        Token token2 = new Token();
        Set<Token> tokens = new HashSet<>();
        tokens.add(token1);
        tokens.add(token2);
        jdbcTokenDao.saveAll(tokens);
        verify(session, times(1)).persist(any(Token.class));
    }

    @Test
    @DisplayName("Should find all tokens")
    void testFindAll() {
        when(session.createQuery(anyString(), eq(Token.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(token));
        List<Token> result = jdbcTokenDao.findAll();
        assertEquals(1, result.size());
        assertEquals(token, result.get(0));
    }
}
