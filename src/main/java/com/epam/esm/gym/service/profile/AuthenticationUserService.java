package com.epam.esm.gym.service.profile;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.SecurityUser;
import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.exception.InvalidJwtAuthenticationException;
import com.epam.esm.gym.security.JwtProvider;
import com.epam.esm.gym.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

import static com.epam.esm.gym.domain.RoleType.ROLE_TRAINER;
import static java.time.Instant.now;

/**
 * Implementation of the {@link AuthenticationService} interface.
 * Provides concrete methods for user authentication, registration, token management, and user details retrieval.
 * Utilizes repositories and services to manage user and token data.
 * Ensures transactional integrity and handles various aspects of user management
 * including login, logout, and token refresh.
 */
@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationUserService implements AuthenticationService {

    private final AuthenticationManager manager;
    private final PasswordEncoder encoder;
    private final JwtProvider provider;
    private final UserDao dao;

    /**
     * {@inheritDoc}
     * Registers a new user and returns an authentication response with access and refresh tokens.
     */
    @Override
    @Transactional
    public AuthenticationResponse signup(final RegisterRequest request) {
        String username = request.getUsername();
        String baseUsername = username;
        int suffix = 1;
        while (dao.findByUsername(username).isPresent()) {
            username = baseUsername + suffix;
            suffix++;
        }
        SecurityUser user = SecurityUser.builder()
                .user(dao.save(getUserWithRole(request, username)))
                .build();
        return getAuthenticationResponse(user);
    }

    /**
     * {@inheritDoc}
     * Authenticates a user based on the provided credentials and returns an authentication response with tokens.
     */
    @Override
    @Transactional
    public AuthenticationResponse login(
            final AuthenticationRequest request) {
        setAuthenticationToken(request);
        SecurityUser user = findUser(request.getUsername());
        return getAuthenticationResponse(user);
    }

    /**
     * {@inheritDoc}
     * Authenticates a user based on the provided credentials and returns an authentication response with tokens.
     */
    @Override
    @Transactional
    public AuthenticationResponse authenticate(
            final AuthenticationRequest request) {
        setAuthenticationToken(request);
        SecurityUser user = findUser(request.getUsername());
        provider.revokeAllUserTokens(user.getUser());
        return getAuthenticationResponse(user);
    }

    /**
     * {@inheritDoc}
     * Refreshes the authentication token based on the provided authorization header
     * and returns a new authentication response.
     */
    @Override
    @Transactional
    public AuthenticationResponse refresh(
            final String authorizationHeader,
            final HttpServletResponse response) {
        if (provider.isBearerToken(authorizationHeader)) {
            String refreshToken = authorizationHeader.substring(7);
            String username = provider.getUsername(refreshToken);
            if (username != null) {
                SecurityUser user = findUser(username);
                if (provider.isTokenValid(refreshToken, user)) {
                    String accessToken = provider.generateToken(user);
                    Token token = provider.updateUserTokens(user, accessToken);
                    return AuthenticationResponse.builder()
                            .username(username)
                            .accessToken(token.getAccessToken())
                            .refreshToken(refreshToken)
                            .expiresAt(new Timestamp(token.getAccessTokenTTL()))
                            .build();
                }
            }
        }
        throw new InvalidJwtAuthenticationException("Invalid Jwt Authentication");
    }

    /**
     * {@inheritDoc}
     * Logs out the user by invalidating the current session and tokens.
     */
    @Override
    public void logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Token token = provider.findByToken(jwt).orElse(null);
            if (token != null) {
                token.setExpired(true);
                token.setRevoked(true);
                provider.save(token);
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().write("Logout successful");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Authenticates a user based on the provided {@link AuthenticationRequest}
     * and sets the authentication in the security context.
     * <p>
     * This method performs several key actions: it first creates a {@link UsernamePasswordAuthenticationToken}
     * using the username and password from the {@link AuthenticationRequest}. It then uses the
     * {@link AuthenticationManager} to authenticate this token. If authentication is successful,
     * the token is set in the {@link SecurityContextHolder}, allowing Spring Security to manage the current
     * authentication. If authentication fails, an exception will be thrown by the {@link AuthenticationManager}.
     * </p>
     *
     * @param request the {@link AuthenticationRequest} containing the username and password to authenticate
     * @throws org.springframework.security.core.AuthenticationException if the authentication fails
     */
    private void setAuthenticationToken(
            final AuthenticationRequest request) {
        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }

    /**
     * {@inheritDoc}
     * Retrieves user details based on the username and returns a {@link SecurityUser} object.
     */
    @Override
    @Transactional
    public SecurityUser findUser(final String username) {
        User user = dao.getUserBy(username);
        return SecurityUser.builder().user(user).build();
    }

    /**
     * {@inheritDoc}
     * Retrieves a user with a specific role based on the registration request and username.
     */
    @Override
    public User getUserWithRole(final RegisterRequest request, String username) {
        return User.builder()
                .password(encoder.encode(request.getPassword()))
                .username(username)
                .permission(ROLE_TRAINER)
                .build();
    }

    /**
     * {@inheritDoc}
     * Finds a user by their username and returns an {@link Optional}.
     */
    @Override
    @Transactional
    public Optional<User> findByUsername(
            final String username) {
        return dao.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     * Constructs an authentication response from a {@link SecurityUser} object.
     */
    @Override
    @Transactional
    public AuthenticationResponse getAuthenticationResponse(final SecurityUser user) {
        String jwtToken = provider.generateToken(user);
        Token token = provider.updateUserTokens(user, jwtToken);
        return getAuthenticationResponse(user, jwtToken, token.getAccessTokenTTL());
    }

    /**
     * {@inheritDoc}
     * Constructs an authentication response from a {@link SecurityUser} and an access token.
     */
    @Override
    public AuthenticationResponse getAuthenticationResponse(
            final SecurityUser user, final String accessToken) {
        return getAuthenticationResponse(user, accessToken, provider.getExpiration());
    }

    /**
     * {@inheritDoc}
     * Constructs an authentication response from user details, a JWT token, and an access token.
     */
    @Override
    public AuthenticationResponse getAuthenticationResponse(
            final UserDetails user,
            final String jwtToken,
            final Long accessToken) {
        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .expiresAt(Timestamp.from(now()
                        .plusMillis(accessToken)))
                .refreshToken(provider.generateRefreshToken(user))
                .accessToken(jwtToken)
                .build();
    }

    /**
     * {@inheritDoc}
     * Generates a token for the provided user details.
     */
    @Override
    public String generateToken(UserDetails user) {
        return provider.generateToken(user);
    }
}
