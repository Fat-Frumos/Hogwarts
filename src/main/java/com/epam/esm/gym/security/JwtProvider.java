package com.epam.esm.gym.security;

import com.epam.esm.gym.dao.TokenDao;
import com.epam.esm.gym.domain.SecurityUser;
import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.TokenType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.exception.InvalidJwtAuthenticationException;
import com.epam.esm.gym.exception.TokenNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtProvider {
    @Getter
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshExpiration;
    private final SecretKey secretKey;
    private final TokenDao tokenRepository;
    private final JwtProperties jwtProperty;
    public static final String TOKEN_PREFIX = "Bearer ";

    public JwtProvider(JwtProperties jwtProperty, TokenDao tokenDao) {
        this.jwtProperty = jwtProperty;
        this.tokenRepository = tokenDao;
        if (jwtProperty.getSecret() == null || jwtProperty.getSecret().isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or empty");
        }
        this.secretKey = Jwts.SIG.HS256.key().build();
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public String generateToken(SecurityUser user, Long expiration) {
        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuer(jwtProperty.getIssuer())
                .subject(String.valueOf(user.getUsername()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .claim("username", user.getUsername())
                .compact();
    }

    public String getUsername(String token, boolean expirationCheck) {
        try {
            return getUsername(getAllClaims(token));
        } catch (ExpiredJwtException e) {
            if (expirationCheck) throw new IllegalArgumentException("Invalid or expired refresh token.");
            else return getUsername(e.getClaims());
        }
    }

    private String getUsername(Claims payload) {
        return (String) payload.get("id");
    }

    public User extractUserInfo(String token) {
        Claims payload = getAllClaims(token);
        return User.builder()
                .id(Integer.valueOf(getUsername(payload)))
                .username((String) payload.get("username"))
                .build();

    }

    public <T> T getClaim(
            final String token,
            final Function<Claims, T> claims) {
        return claims.apply(getAllClaims(token));
    }

    public String generateRefreshToken(
            final UserDetails user) {
        return generateToken(new HashMap<>(), user, refreshExpiration);
    }

    public String generateToken(
            final UserDetails userDetails) {
        return generateToken(new HashMap<>(),
                userDetails);
    }

    public String generateToken(
            final Map<String, Object> claims,
            final @NotNull UserDetails user) {
        return generateToken(claims, user, expiration);
    }

    private String generateToken(
            final Map<String, Object> claims,
            final UserDetails user,
            final Long expiration) {
        try {
            return Jwts.builder()
                    .signWith(secretKey, Jwts.SIG.HS256)
                    .issuer(jwtProperty.getIssuer())
                    .subject(String.valueOf(user.getUsername()))
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .claim("username", user.getUsername())
                    .claims(claims)
                    .compact();
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Invalid jwt authentication");
        }
    }

    public boolean isTokenValid(
            final String token,
            final UserDetails userDetails) {
        try {
            return getUsername(token)
                    .equals(userDetails.getUsername())
                    && !getClaim(token, Claims::getExpiration)
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getAllClaims(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException
                 | ExpiredJwtException
                 | UnsupportedJwtException
                 | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException(e.getMessage());
        }
    }

    public boolean isBearerToken(
            final String authorizationHeader) {
        return authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ");
    }

    @Transactional
    public List<Token> findAllValidToken(final User user) {
        return tokenRepository.findAllValidAccessTokenByUserId(user.getId());
    }

    @Transactional
    public Token updateUserTokens(
            final User user,
            final String accessToken) {
        Token token = getToken(user, accessToken);
        return save(token);
    }

    @Transactional
    public void revokeAllUserTokens(final User user) {
        List<Token> tokens = findAllValidToken(user);
        if (!tokens.isEmpty()) {
            tokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
        }
        tokenRepository.saveAll(tokens);
    }

    @Transactional
    public Optional<Token> findByToken(final String jwt) {
        return tokenRepository.findByAccessToken(jwt);
    }

    public Token getToken(
            final User user,
            final String accessToken) {
        return Token.builder()
                .user(user)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.BEARER)
                .accessToken(accessToken)
                .accessTokenTTL(getExpiration())
                .build();
    }

    public String getUsername(
            final String token) {
        try {
            return getClaim(token, Claims::getSubject);
        } catch (MalformedJwtException e) {
            throw new TokenNotFoundException(
                    String.format("Token not found: %s", token));
        }
    }

    public Token save(Token token) {
        return tokenRepository.save(token);
    }
}
