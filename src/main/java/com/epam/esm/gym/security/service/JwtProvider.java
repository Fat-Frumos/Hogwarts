package com.epam.esm.gym.security.service;

import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.UserPrincipal;
import com.epam.esm.gym.exception.InvalidJwtAuthenticationException;
import com.epam.esm.gym.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.time.Instant.now;

/**
 * Service for handling JWT (JSON Web Token) operations.
 * <p>
 * This class provides functionality for generating, parsing, and validating JWTs.
 * It supports both access and refresh tokens, managing token claims and expiration.
 * It interacts with the {@link com.epam.esm.gym.service.TokenService} to save, retrieve, and update tokens.
 * Additionally, it provides methods to verify token validity and handle token-related exceptions.
 * </p>
 */
@Service
public class JwtProvider {
    private final JwtProperties jwtProperty;
    private final TokenService tokenService;
    private final String secretKey;

    /**
     * Constructs a new JwtProvider with the provided JWT properties and token service.
     * Initializes the secret key used for signing JWTs.
     * Throws an exception if the secret key is null or empty.
     * This ensures that JWTs cannot be generated or validated without a valid secret key.
     * It uses the properties to set up the necessary configurations for JWT operations.
     *
     * @param jwtProperty  the JWT properties containing issuer and secret key information
     * @param tokenService the token service for interacting with tokens
     * @throws InvalidJwtAuthenticationException if the entity is null.
     */
    public JwtProvider(JwtProperties jwtProperty, TokenService tokenService) {
        this.jwtProperty = jwtProperty;
        this.tokenService = tokenService;

        try {
            secretKey = Base64.getEncoder().encodeToString(
                    KeyGenerator.getInstance("HmacSHA256")
                            .generateKey()
                            .getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidJwtAuthenticationException(e.getMessage());
        }
    }

    /**
     * Generates a JWT token with the specified claims and expiration time.
     * This method supports creating tokens with custom claims and expiration settings.
     * It builds the token with a specified expiration time, user details, and claims.
     * This method is used for both access and refresh tokens based on the provided expiration.
     * It ensures that the token contains the required information for authentication.
     *
     * @param claims   the claims to include in the token
     * @param username the username
     * @return the generated JWT token
     */
    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .and()
                .signWith(getKey())
                .compact();

    }

    /**
     * Generates a SecretKey instance using the provided secret key string.
     * This method decodes the Base64 encoded secret key and creates an HMAC SHA key.
     *
     * @return the SecretKey used for signing and verifying JWT tokens
     */
    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username from the provided JWT token.
     * Otherwise, it retrieves the username from the claims of the expired token.
     * This method helps in managing token validation and expiration scenarios.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Retrieves a specific claim from the JWT token using a provided function.
     * Applies the function to extract the desired claim from the JWT claims.
     * This method allows for flexible retrieval of various claims from the token.
     * It is useful for accessing different pieces of information embedded in the JWT.
     * Ensures that the claims are parsed and retrieved correctly for further processing.
     *
     * @param token         the JWT token
     * @param claimResolver a function to extract the claim from the JWT claims
     * @param <T>           the type of the claim
     * @return the extracted claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        return claimResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
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
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks whether the provided JWT token has expired.
     *
     * @param token the JWT token to check
     * @return {@code true} if the token has expired, otherwise {@code false}
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the provided JWT token.
     *
     * @param token the JWT token from which to extract the expiration date
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
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
        Set<Token> tokens = tokenService.findAllValidAccessTokenByUserId(user.getId());
        if (!tokens.isEmpty()) {
            tokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
        }
        tokenService.saveAll(tokens);
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
     * Generates a JWT token with the specified claims and expiration time.
     * This method supports creating tokens with custom claims and expiration settings.
     * It builds the token with a specified expiration time, user details, and claims.
     * This method is used for both access and refresh tokens based on the provided expiration.
     * It ensures that the token contains the required information for authentication.
     *
     * @param userDetails the user details
     * @return the generated JWT token
     */
    public String generateToken(UserPrincipal userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.user().getPermission());
        return generateToken(userDetails.getUsername(), claims);
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
            final UserPrincipal user,
            final String accessToken) {
        Token token = getToken(user, accessToken);
        return tokenService.save(token);
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
            final UserPrincipal user,
            final String accessToken) {
        return Token.builder()
                .user(user.user())
                .expired(false)
                .revoked(false)
                .tokenType(Token.TokenType.BEARER)
                .accessToken(accessToken)
                .accessTokenTTL(jwtProperty.getExpiration())
                .build();
    }

    /**
     * Generates an {@link AuthenticationResponse} based on user details and JWT token.
     * Creates a response that includes the JWT token, user details, and access token.
     * This method is used to return comprehensive authentication data including token details.
     * Supports scenarios where detailed response data is required for authenticated users.
     * Ensures that all relevant information is included in the authentication response.
     *
     * @param user        The user details to include in the response.
     * @param jwtToken    The JWT token to include in the response.
     * @param accessToken The access token to include in the response.
     * @return An {@link AuthenticationResponse} containing token and user information.
     */
    public AuthenticationResponse getAuthenticationResponse(UserPrincipal user, String jwtToken, Long accessToken) {
        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .expiresAt(Timestamp.from(now()
                        .plusMillis(accessToken)))
                .refreshToken(generateToken(user))
                .accessToken(jwtToken)
                .build();
    }
}
