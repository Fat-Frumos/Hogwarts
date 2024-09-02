package com.epam.esm.gym.security;

import com.epam.esm.gym.domain.Token;

import java.util.List;
import java.util.Optional;

public interface TokenService {
    List<Token> findAllValidAccessTokenByUserId(Integer id);

    void saveAll(List<Token> tokens);

    Optional<Token> findByAccessToken(String jwt);

    Token save(Token token);
}
