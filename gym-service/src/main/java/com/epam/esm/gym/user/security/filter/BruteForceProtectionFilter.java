package com.epam.esm.gym.user.security.filter;

import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * A filter that provides protection against brute-force attacks by limiting
 * the number of failed login attempts for a given username.
 *
 * <p>This filter intercepts login requests and checks if the username is locked due
 * to too many failed login attempts. If the username is locked, it responds with
 * an HTTP 403 Forbidden status.</p>
 */
@Service
@AllArgsConstructor
public class BruteForceProtectionFilter extends OncePerRequestFilter {

    private final BruteForceProtectionService bruteForceProtectionService;

    /**
     * Processes the request to check if the username has been locked due to
     * excessive failed login attempts.
     *
     * <p>If the username is locked, a 403 Forbidden response is sent. Otherwise,
     * the request is passed along the filter chain.</p>
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param chain    the filter chain
     * @throws ServletException if a servlet exception occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {

        String username = request.getParameter("username");

        if (username != null && bruteForceProtectionService.isLocked(username)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Too many failed login attempts");
            return;
        }
        chain.doFilter(request, response);
    }
}
