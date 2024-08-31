package com.epam.esm.gym.security.handler;

import com.epam.esm.gym.security.BruteForceProtectionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class BruteForceAuthenticationFailureHandler implements AuthenticationFailureHandler, AccessDeniedHandler {

    private final BruteForceProtectionService protectionService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        handleFailure(request, response, exception);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        handleFailure(request, response, exception);
    }

    private void handleFailure(HttpServletRequest request, HttpServletResponse response, RuntimeException exception) throws IOException {
        String username = request.getParameter("username");
        if (username != null) {
            protectionService.registerFailedAttempt(username);
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
    }
}
