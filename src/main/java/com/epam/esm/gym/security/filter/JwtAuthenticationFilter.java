package com.epam.esm.gym.security.filter;

import com.epam.esm.gym.dto.auth.UserPrincipal;
import com.epam.esm.gym.exception.InvalidJwtAuthenticationException;
import com.epam.esm.gym.exception.UnauthorizedAccessException;
import com.epam.esm.gym.security.service.JwtProvider;
import com.epam.esm.gym.security.service.SecurityUserDetailsService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that processes JWT authentication by extracting and validating the JWT token
 * from the request header and setting the authentication in the security context.
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final ApplicationContext context;

    /**
     * This method is called once per request to filter and authenticate the request
     * using JWT tokens.
     *
     * @param request     the {@link HttpServletRequest} object.
     * @param response    the {@link HttpServletResponse} object.
     * @param filterChain the {@link FilterChain} to pass the request and response to the next filter.
     * @throws ServletException if an error occurs during the request handling.
     * @throws IOException      if an I/O error occurs during the request handling.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtProvider.extractUserName(token);
            }
        } catch (SignatureException exception) {
            throw new InvalidJwtAuthenticationException(exception.getMessage());
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserPrincipal userDetails = context.getBean(SecurityUserDetailsService.class).loadUserByUsername(username);
            if (jwtProvider.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            throw new UnauthorizedAccessException(e.getMessage());
        }
    }
}
