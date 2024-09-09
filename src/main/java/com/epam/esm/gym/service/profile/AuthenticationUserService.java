package com.epam.esm.gym.service.profile;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.auth.MessageResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.dto.auth.UserPrincipal;
import com.epam.esm.gym.exception.InvalidJwtAuthenticationException;
import com.epam.esm.gym.security.service.JwtProvider;
import com.epam.esm.gym.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.gym.domain.RoleType.ROLE_TRAINER;

/**
 * Implementation of the {@link AuthenticationService} interface.
 * Provides concrete methods for user authentication, registration, token management, and user details retrieval.
 * Utilizes repositories and services to manage user and token data.
 * Ensures transactional integrity and handles various aspects of user management
 * including login, logout, and token refresh.
 */
@Service
@AllArgsConstructor
public class AuthenticationUserService implements AuthenticationService {

    private final AuthenticationManager manager;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final UserDao dao;

    /**
     * {@inheritDoc}
     * Registers a new user and returns an authentication response with access and refresh tokens.
     */
    @Override
    @Transactional
    public ResponseEntity<BaseResponse> signup(final RegisterRequest request) {
        String baseUsername = request.getUsername();
        List<User> existingUsernames = dao.findUsernamesByBaseName(request.getUsername());
        int suffix = existingUsernames.stream()
                .map(user -> user.getUsername().replace(baseUsername + ".", ""))
                .filter(s -> s.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;
        User saved = dao.save(getUserWithRole(request, baseUsername + "." + suffix));
        UserPrincipal user = UserPrincipal.builder().user(saved).build();
        return ResponseEntity.ok(getAuthenticationResponse(user));
    }

    /**
     * {@inheritDoc}
     * Authenticates a user based on the provided credentials and returns an authentication response with tokens.
     */
    @Override
    @Transactional
    public ResponseEntity<BaseResponse> login(
            final AuthenticationRequest request) {
        Optional<User> userOptional = dao.findByUsername(request.getUsername());
        if (userOptional.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("User not found " + request.getUsername()));
        }
        UserPrincipal user = UserPrincipal.builder().user(userOptional.get()).build();
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtProvider.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(token)
                    .username(request.getUsername())
                    .refreshToken(token)
                    .expiresAt(new java.sql.Timestamp(System.currentTimeMillis() + 60 * 60 * 1000)).build());
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Failed Authenticates login"));
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
        UserPrincipal user = findUser(request.getUsername());
        jwtProvider.revokeAllUserTokens(user.user());
        String jwtToken = jwtProvider.generateToken(user);
        Token token = jwtProvider.updateUserTokens(user, jwtToken);
        return jwtProvider.getAuthenticationResponse(user, jwtToken, token.getAccessTokenTTL());
    }

    /**
     * {@inheritDoc}
     * Refreshes the authentication token based on the provided authorization header
     * and returns a new authentication response.
     */
    @Override
    @Transactional
    public ResponseEntity<BaseResponse> refresh(
            final String authorizationHeader,
            final HttpServletResponse response) {
        if (jwtProvider.isBearerToken(authorizationHeader)) {
            String refreshToken = authorizationHeader.substring(7);
            String username = jwtProvider.extractUserName(refreshToken);
            if (username != null) {
                UserPrincipal user = UserPrincipal.builder().user(dao.getUserBy(username)).build();
                if (jwtProvider.validateToken(refreshToken, user)) {
                    String accessToken = jwtProvider.generateToken(user);
                    Token token = jwtProvider.updateUserTokens(user, accessToken);
                    return ResponseEntity.ok(AuthenticationResponse.builder()
                            .username(username)
                            .accessToken(token.getAccessToken())
                            .refreshToken(refreshToken)
                            .expiresAt(new Timestamp(token.getAccessTokenTTL()))
                            .build());
                }
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid Jwt Authentication"));
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
            Token token = jwtProvider.findByToken(jwt).orElse(null);
            if (token != null) {
                token.setExpired(true);
                token.setRevoked(true);
                jwtProvider.save(token);
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().write("Logout successful");
        } catch (IOException e) {
            throw new InvalidJwtAuthenticationException(authHeader);
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
     * @throws BadCredentialsException if the authentication fails
     */
    private void setAuthenticationToken(
            final AuthenticationRequest request) {
        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        if (authenticate.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        } else {
            throw new BadCredentialsException("Authentication failed");
        }
    }

    /**
     * {@inheritDoc}
     * Retrieves user details based on the username and returns a {@link UserPrincipal} object.
     */
    @Override
    @Transactional
    public UserPrincipal findUser(final String username) {
        User user = dao.getUserBy(username);
        return UserPrincipal.builder().user(user).build();
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
     * Constructs an authentication response from a {@link UserPrincipal} object.
     */
    @Override
    @Transactional
    public AuthenticationResponse getAuthenticationResponse(final UserPrincipal user) {
        String jwtToken = jwtProvider.generateToken(user);
        Token token = jwtProvider.updateUserTokens(user, jwtToken);
        return jwtProvider.getAuthenticationResponse(user, jwtToken, token.getAccessTokenTTL());
    }
}
