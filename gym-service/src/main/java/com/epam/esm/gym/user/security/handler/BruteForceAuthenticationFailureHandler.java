package com.epam.esm.gym.user.security.handler;

import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Handles authentication and access denial failures with brute-force protection.
 *
 * <p>This component implements both {@link AuthenticationFailureHandler}
 * and {@link AccessDeniedHandler} to manage authentication failures and access denial events.
 * It interacts with {@link BruteForceProtectionService} to register
 * failed authentication attempts and applies brute-force protection measures.</p>
 */
@Component
@AllArgsConstructor
public class BruteForceAuthenticationFailureHandler
        implements AuthenticationFailureHandler, AccessDeniedHandler {

    private final BruteForceProtectionService protectionService;

    /**
     * Handles access denial events by registering failed attempts
     * and sending an unauthorized error response.
     *
     * <p>This method is called when a user is denied access to a resource.
     * It delegates to the {@link #handleFailure}
     * method to handle the failure by registering the denied access attempt
     * and sending an appropriate HTTP error response.</p>
     *
     * @param request   the {@link HttpServletRequest} containing request information.
     * @param response  the {@link HttpServletResponse} to send the error response.
     * @param exception the {@link AccessDeniedException} representing the access denial.
     * @throws IOException if an I/O error occurs while sending the error response.
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception) throws IOException {
        handleFailure(request, response, exception);
    }

    /**
     * Handles authentication failure events by registering failed attempts and sending an unauthorized error response.
     *
     * <p>This method is called when authentication fails. It delegates to the {@link #handleFailure} method to handle
     * the failure by registering the failed authentication attempt and sending an appropriate HTTP error response.</p>
     *
     * @param request   the {@link HttpServletRequest} containing request information.
     * @param response  the {@link HttpServletResponse} to send the error response.
     * @param exception the {@link AuthenticationException} representing the authentication failure.
     * @throws IOException if an I/O error occurs while sending the error response.
     */
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        handleFailure(request, response, exception);
    }

    /**
     * Registers a failed authentication or access attempt and sends an unauthorized error response.
     *
     * <p>This private method is used to handle both authentication failures and access denials.
     * It retrieves the username from the request, registers the failed attempt with
     * the {@link BruteForceProtectionService}, and sends an HTTP
     * unauthorized error response with a message containing the exception details.</p>
     *
     * @param request   the {@link HttpServletRequest} containing request information.
     * @param response  the {@link HttpServletResponse} to send the error response.
     * @param exception the {@link RuntimeException} representing the failure.
     * @throws IOException if an I/O error occurs while sending the error response.
     */
    private void handleFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            RuntimeException exception) throws IOException {
        String username = request.getParameter("username");
        if (username != null) {
            protectionService.registerFailedAttempt(username);
        }
        response.sendError(SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
    }
}
