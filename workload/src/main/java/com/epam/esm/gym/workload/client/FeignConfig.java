package com.epam.esm.gym.workload.client;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * Configuration class for Feign clients.
 * Provides a RestTemplate bean for HTTP communication.
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class FeignConfig {

    private final RestTemplate restTemplate;
    /**
     * Creates a custom ErrorDecoder for handling Feign client errors.
     *
     * @return An ErrorDecoder that converts HTTP 500 responses into
     * an EntityNotFoundException with a specific error message.
     * All other responses are handled by the default error decoder.
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            if (response.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return new EntityNotFoundException("An unexpected error occurred.");
            } else {
                return new ErrorDecoder.Default().decode(methodKey, response);
            }
        };
    }

    /**
     * Creates a RequestInterceptor bean to add an Authorization header
     * with a Bearer token to outgoing Feign client requests.
     *
     * @return a RequestInterceptor that modifies the request template
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", "Bearer " + getAccessToken());
    }

    /**
     * Retrieves the access token by making a login request to the authentication service.
     *
     * @return the access token as a String
     */
    protected String getAccessToken() {
        String baseUrl = "http://localhost:8090";
        String url = baseUrl + "/api/auth/login";
        String requestBody = "{\"username\": \"Lord.Voldemort\", \"password\": \"You-Know-Who\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return Objects.requireNonNull(response.getBody()).get("access_token");
        } catch (HttpClientErrorException e) {
            log.error("Error during authentication: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
            return "";
        }
    }
}
