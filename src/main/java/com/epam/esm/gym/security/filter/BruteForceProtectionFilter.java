package com.epam.esm.gym.security.filter;

import com.epam.esm.gym.security.BruteForceProtectionService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Service
@AllArgsConstructor
public class BruteForceProtectionFilter extends OncePerRequestFilter {

    private final BruteForceProtectionService bruteForceProtectionService;

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
