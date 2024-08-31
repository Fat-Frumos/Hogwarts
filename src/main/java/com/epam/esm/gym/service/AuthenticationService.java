package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.Role;
import com.epam.esm.gym.domain.SecurityUser;
import com.epam.esm.gym.domain.Token;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.exception.InvalidJwtAuthenticationException;
import com.epam.esm.gym.exception.UserNotFoundException;
import com.epam.esm.gym.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

import static com.epam.esm.gym.domain.RoleType.TRAINER;
import static java.lang.String.format;
import static java.time.Instant.now;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService implements AuthService {

    private final AuthenticationManager manager;
    private final PasswordEncoder encoder;
    private final UserDao userRepository;
    private final JwtProvider provider;

    @Transactional
    public AuthenticationResponse signup(final RegisterRequest request) {
        String username = request.getUsername();
        String baseUsername = username;
        int suffix = 1;
        while (findByUsername(username).isPresent()) {
            username = baseUsername + suffix;
            suffix++;
        }
        SecurityUser user = SecurityUser.builder()
                .user(saveUserWithRole(request, username))
                .build();
        return getAuthenticationResponse(user);
    }

    @Transactional
    public AuthenticationResponse login(
            final AuthenticationRequest request) {
        setAuthenticationToken(request);
        SecurityUser user = findUser(request.getUsername());
        return getAuthenticationResponse(user);
    }

    @Transactional
    public AuthenticationResponse authenticate(
            final AuthenticationRequest request) {
        setAuthenticationToken(request);
        SecurityUser user = findUser(request.getUsername());
        provider.revokeAllUserTokens(user.getUser());
        return getAuthenticationResponse(user);
    }

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
                    Token token = provider.updateUserTokens(user.getUser(), accessToken);
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

    private void setAuthenticationToken(
            final AuthenticationRequest request) {
        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }

    @Transactional
    public SecurityUser findUser(final String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        format("User not found: %s", username)));
        return SecurityUser.builder().user(user).build();
    }

    @Transactional
    public User saveUserWithRole(final RegisterRequest request, String username) {
        User user = User.builder()
                .password(encoder.encode(request.getPassword()))
                .username(username)
                .role(getRole())
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> findByUsername(
            final String username) {
        return userRepository.findByUsername(username);
    }

    private static Role getRole() {
        return Role.builder()
                .permission(TRAINER)
                .build();
    }

    @Transactional
    public AuthenticationResponse getAuthenticationResponse(final SecurityUser user) {
        String jwtToken = getAccessToken(user);
        Token token = provider.updateUserTokens(user.getUser(), jwtToken);
        return getAuthenticationResponse(user, jwtToken, token.getAccessTokenTTL());
    }

    public AuthenticationResponse getAuthenticationResponse(
            final SecurityUser user, final String accessToken) {
        return getAuthenticationResponse(user, accessToken, provider.getExpiration());
    }

    public AuthenticationResponse getAuthenticationResponse(
            final SecurityUser user,
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

    public String getAccessToken(SecurityUser user) {
        return provider.generateToken(user);
    }
}
