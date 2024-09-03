package com.epam.esm.gym.security.filter;

import com.epam.esm.gym.security.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that processes JWT-based authentication.
 *
 * <p>This filter extracts the JWT token from the Authorization header, validates
 * the token, and sets the authentication in the SecurityContext if the token is valid.</p>
 */
@Slf4j
@Service
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtProvider provider;

    private UserDetailsService userDetailsService;

    /**
     * Processes the request to perform JWT authentication.
     *
     * <p>If a valid JWT token is found, it sets the authentication in the
     * SecurityContext, allowing the request to proceed with authenticated access.</p>
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet exception occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final @NonNull HttpServletResponse response,
            final @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(provider.getUsername(jwt));
            log.info(userDetails.toString());
            log.info(jwt);
            boolean isValid = provider.findByToken(jwt)
                    .map(token -> !token.isExpired()
                            && !token.isRevoked())
                    .orElse(false);
            if (isValid && provider.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null,
                                userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                securityContext.setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
