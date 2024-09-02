package com.epam.esm.gym.security;

import com.epam.esm.gym.dao.TokenDao;
import com.epam.esm.gym.domain.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JDBCTokenService implements TokenService {

    private final TokenDao dao;

    @Override
    public List<Token> findAllValidAccessTokenByUserId(Integer id) {
        return dao.findAllValidAccessTokenByUserId(id);
    }

    @Override
    public void saveAll(List<Token> tokens) {
        dao.saveAll(tokens);
    }

    @Override
    public Optional<Token> findByAccessToken(String jwt) {
        return dao.findByAccessToken(jwt);
    }

    @Override
    public Token save(Token token) {
        return dao.save(token);
    }
}
