package com.epam.esm.gym.security;

import com.epam.esm.gym.domain.SecurityUser;
import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.exception.InvalidJwtAuthenticationException;
import com.epam.esm.gym.exception.TokenNotFoundException;
import com.epam.esm.gym.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Service for handling JWT (JSON Web Token) operations.
 * <p>
 * This class provides functionality for generating, parsing, and validating JWTs.
 * It supports both access and refresh tokens, managing token claims and expiration.
 * It interacts with the {@link TokenService} to save, retrieve, and update tokens.
 * Additionally, it provides methods to verify token validity and handle token-related exceptions.
 * </p>
 */
@Slf4j
@Service
public class JwtProvider {
    @Getter
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshExpiration;
    private final SecretKey secretKey;
    private final TokenService tokenService;
    private final JwtProperties jwtProperty;
    private static final String USERNAME = "username";

    /**
     * Constructs a new JwtProvider with the provided JWT properties and token service.
     * Initializes the secret key used for signing JWTs.
     * Throws an exception if the secret key is null or empty.
     * This ensures that JWTs cannot be generated or validated without a valid secret key.
     * It uses the properties to set up the necessary configurations for JWT operations.
     *
     * @param jwtProperty  the JWT properties containing issuer and secret key information
     * @param tokenService the token service for interacting with tokens
     * @throws IllegalArgumentException if the entity is null.
     */
    public JwtProvider(JwtProperties jwtProperty, TokenService tokenService) {
        this.jwtProperty = jwtProperty;
        this.tokenService = tokenService;
        if (jwtProperty.getSecret() == null || jwtProperty.getSecret().isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or empty");
        }
        this.secretKey = Jwts.SIG.HS256.key().build();
    }

    /**
     * Extracts the username from the provided JWT token.
     * Handles expired tokens based on the expiration check flag.
     * Throws an exception if the token is expired and the flag is set to true.
     * Otherwise, it retrieves the username from the claims of the expired token.
     * This method helps in managing token validation and expiration scenarios.
     *
     * @param token           the JWT token
     * @param expirationCheck flag indicating whether to check for token expiration
     * @return the username extracted from the token
     * @throws IllegalArgumentException if the entity is null.     *
     */
    public String getUsername(String token, boolean expirationCheck) {
        try {
            return getUsername(getAllClaims(token));
        } catch (ExpiredJwtException e) {
            if (expirationCheck) {
                throw new IllegalArgumentException("Invalid or expired refresh token.");
            } else {
                return getUsername(e.getClaims());
            }
        }
    }

    /**
     * Retrieves the username claim from the provided JWT claims.
     * This method casts the claim to a String and returns it.
     * It extracts the username from the token payload for validation and user identification.
     * This method is used internally to access user information from JWT claims.
     * It is crucial for retrieving user details during authentication and authorization.
     *
     * @param payload the JWT claims
     * @return the username from the claims
     */
    private String getUsername(Claims payload) {
        return (String) payload.get(USERNAME);
    }

    /**
     * Retrieves a specific claim from the JWT token using a provided function.
     * Applies the function to extract the desired claim from the JWT claims.
     * This method allows for flexible retrieval of various claims from the token.
     * It is useful for accessing different pieces of information embedded in the JWT.
     * Ensures that the claims are parsed and retrieved correctly for further processing.
     *
     * @param token  the JWT token
     * @param claims a function to extract the claim from the JWT claims
     * @param <T>    the type of the claim
     * @return the extracted claim
     */
    public <T> T getClaim(
            final String token,
            final Function<Claims, T> claims) {
        return claims.apply(getAllClaims(token));
    }

    /**
     * Generates a new refresh token for the given user details.
     * The refresh token has a longer expiration time compared to access tokens.
     * This method creates a JWT token with the refresh expiration setting.
     * It includes user details and other claims to ensure the token's validity.
     * This method is essential for managing user sessions and refreshing authentication tokens.
     *
     * @param user the user details
     * @return the generated refresh token
     */
    public String generateRefreshToken(
            final UserDetails user) {
        return generateToken(new HashMap<>(), user, refreshExpiration);
    }

    /**
     * Generates a new access token for the given user details.
     * The access token is used for authenticating user requests.
     * This method creates a JWT token with the access expiration setting.
     * It includes user details and other claims to ensure token validity.
     * It is crucial for providing secure access to protected resources.
     *
     * @param userDetails the user details
     * @return the generated access token
     */
    public String generateToken(
            final UserDetails userDetails) {
        return generateToken(new HashMap<>(),
                userDetails, expiration);
    }

    /**
     * Generates a JWT token with the specified claims and expiration time.
     * This method supports creating tokens with custom claims and expiration settings.
     * It builds the token with a specified expiration time, user details, and claims.
     * This method is used for both access and refresh tokens based on the provided expiration.
     * It ensures that the token contains the required information for authentication.
     *
     * @param claims      the claims to include in the token
     * @param userDetails the user details
     * @param expiration  the expiration time in seconds
     * @return the generated JWT token
     */
    private String generateToken(
            final Map<String, Object> claims,
            final UserDetails userDetails,
            final Long expiration) {
        try {
            return Jwts.builder()
                    .signWith(secretKey, Jwts.SIG.HS256)
                    .issuer(jwtProperty.getIssuer())
                    .subject(String.valueOf(userDetails.getUsername()))
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .claim(USERNAME, userDetails.getUsername())
                    .claims(claims)
                    .compact();
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException(e.getMessage());
        }
    }

    /**
     * Checks if the provided JWT token is valid for the given user details.
     * This method verifies that the token's username matches the provided user details.
     * It also checks if the token has not expired by comparing the current date with the token's expiration.
     * Handles exceptions that may occur during token validation.
     * This method ensures that only valid and correctly associated tokens are accepted.
     *
     * @param token       the JWT token
     * @param userDetails the user details
     * @return true if the token is valid, otherwise false
     */
    public boolean isTokenValid(final String token, final UserDetails userDetails) {
        try {
            return getUsername(token).equals(userDetails.getUsername())
                    && !getClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves all claims from the provided JWT token.
     * This method parses the token and extracts its claims.
     * Handles various exceptions related to JWT parsing and validation.
     * Ensures that claims are correctly retrieved for further processing.
     * This method is crucial for accessing token data and validating JWTs.
     *
     * @param token the JWT token
     * @return the claims extracted from the token
     * @throws InvalidJwtAuthenticationException if the catch a error.
     */
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

    /**
     * Checks if the provided authorization header contains a Bearer token.
     * This method verifies if the header starts with "Bearer ".
     * It is used to ensure that the correct type of authorization token is being used.
     * This method helps in validating authorization headers in requests.
     * It is essential for identifying and processing bearer tokens in authentication flows.
     *
     * @param authorizationHeader the authorization header
     * @return true if the header contains a Bearer token, otherwise false
     */
    public boolean isBearerToken(
            final String authorizationHeader) {
        return authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ");
    }

    /**
     * Finds all valid access tokens for the given user.
     * This method interacts with the TokenService to retrieve tokens by user ID.
     * It filters tokens to return only those that are valid.
     * This method is used for managing and validating user tokens.
     * Ensures that only tokens with valid statuses are considered.
     *
     * @param user the user whose tokens are to be retrieved
     * @return a list of valid tokens for the user
     */
    @Transactional
    public List<Token> findAllValidToken(final User user) {
        return tokenService.findAllValidAccessTokenByUserId(user.getId());
    }

    /**
     * Updates the tokens for the given user with the specified access token.
     * This method interacts with the TokenService to retrieve and save the token.
     * It creates a Token object with the provided access token and updates its details.
     * Ensures that user tokens are correctly updated and persisted.
     * This method helps in managing token lifecycles and user sessions.
     *
     * @param user        the security user
     * @param accessToken the new access token
     * @return the updated token
     */
    @Transactional
    public Token updateUserTokens(
            final SecurityUser user,
            final String accessToken) {
        Token token = getToken(user, accessToken);
        return save(token);
    }

    /**
     * Revokes all tokens for the specified user.
     * This method updates the status of each token to expired and revoked.
     * It interacts with the TokenService to save the updated tokens.
     * Ensures that all tokens for the user are invalidated effectively.
     * This method is used to manage token revocation and user session termination.
     *
     * @param user the user whose tokens are to be revoked
     */
    @Transactional
    public void revokeAllUserTokens(final User user) {
        List<Token> tokens = findAllValidToken(user);
        if (!tokens.isEmpty()) {
            tokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
        }
        tokenService.saveAll(tokens);
    }

    /**
     * Finds a token by its JWT string.
     * This method interacts with the TokenService to retrieve the token.
     * It returns an Optional containing the token if found.
     * Ensures that tokens can be located and managed based on their JWT string.
     * This method is used for token validation and management purposes.
     *
     * @param jwt the JWT token string
     * @return an Optional containing the token if found
     */
    @Transactional
    public Optional<Token> findByToken(final String jwt) {
        return tokenService.findByAccessToken(jwt);
    }

    /**
     * Creates a new Token object with the specified user and access token details.
     * This method builds a Token instance with the provided user and token information.
     * It sets the token type, expiration, and other necessary details.
     * This method is used for creating and managing tokens within the system.
     * Ensures that the token is accurately represented and ready for use.
     *
     * @param user        the security user
     * @param accessToken the access token string
     * @return the created Token object
     */
    public Token getToken(
            final SecurityUser user,
            final String accessToken) {
        return Token.builder()
                .user(user.getUser())
                .expired(false)
                .revoked(false)
                .tokenType(Token.TokenType.BEARER)
                .accessToken(accessToken)
                .accessTokenTTL(getExpiration())
                .build();
    }

    /**
     * Retrieves the username from the provided JWT token.
     * This method extracts the username from the token's subject claim.
     * It handles cases where the token is malformed and throws an exception if necessary.
     * This method is used for validating and retrieving user information from tokens.
     * Ensures that username extraction is handled securely and accurately.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     * @throws TokenNotFoundException if there is token fon found
     */
    public String getUsername(final String token) {
        try {
            return getClaim(token, Claims::getSubject);
        } catch (MalformedJwtException e) {
            throw new TokenNotFoundException(
                    String.format("Token not found: %s", token));
        }
    }

    /**
     * Saves the provided Token object using the TokenService.
     * This method interacts with the TokenService to persist the token data.
     * It ensures that the token is stored correctly for future use.
     * This method is used for managing tokens within the system.
     * Ensures that tokens are accurately saved and available for retrieval.
     *
     * @param token the Token object to be saved
     * @return the saved token
     */
    public Token save(Token token) {
        return tokenService.save(token);
    }
}
