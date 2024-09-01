package com.epam.esm.gym.security.handler;

import com.epam.esm.gym.domain.SecurityUser;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class SuccessAuthenticationHandler implements AuthenticationSuccessHandler {

    private final AuthenticationService authenticationService;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
        String accessToken = authenticationService.generateToken(userDetails);
        AuthenticationResponse authResponse = authenticationService.getAuthenticationResponse(userDetails, accessToken);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(convertToJson(authResponse));
    }

    private String convertToJson(AuthenticationResponse authResponse) {
        try {
            return objectMapper.writeValueAsString(authResponse);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
