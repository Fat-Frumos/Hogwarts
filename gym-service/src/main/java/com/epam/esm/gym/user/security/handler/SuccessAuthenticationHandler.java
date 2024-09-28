package com.epam.esm.gym.user.security.handler;

import com.epam.esm.gym.user.entity.Token;
import com.epam.esm.gym.user.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.user.dto.auth.UserPrincipal;
import com.epam.esm.gym.user.security.service.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Handles successful authentication by generating an authentication response and sending it to the client.
 * <p>
 * This service is triggered upon successful authentication. It generates an access token, creates an
 * authentication response containing the token and other relevant details, and sends it back to the client
 * in JSON format.
 * </p>
 */
@Service
@AllArgsConstructor
public class SuccessAuthenticationHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    /**
     * Handles successful authentication by generating and sending an authentication response.
     * <p>
     * This method retrieves user details from the authentication object, generates an access token, and creates
     * an `AuthenticationResponse` object. It then sets the response content type to JSON, sets the HTTP status
     * to 200 (OK), and writes the JSON representation of the authentication response to the response output stream.
     * </p>
     *
     * @param request        the HTTP request associated with the authentication attempt
     * @param response       the HTTP response to send back to the client
     * @param authentication the successful authentication object containing user details
     * @throws IOException if an I/O error occurs while writing to the response
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        String jwtToken = jwtProvider.generateToken(user);
        Token token = jwtProvider.updateUserTokens(user, jwtToken);
        AuthenticationResponse authResponse = jwtProvider
                .getAuthenticationResponse(user, jwtToken, token.getAccessTokenTTL());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(convertToJson(authResponse));
    }

    /**
     * Converts the given authentication response to a JSON string.
     * <p>
     * This method uses the `ObjectMapper` to serialize the `AuthenticationResponse` object into a JSON string.
     * If serialization fails due to a `JsonProcessingException`, an empty JSON object ("{}") is returned.
     * </p>
     *
     * @param authResponse the authentication response to convert to JSON
     * @return the JSON representation of the authentication response
     */
    private String convertToJson(AuthenticationResponse authResponse) {
        try {
            return objectMapper.writeValueAsString(authResponse);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
