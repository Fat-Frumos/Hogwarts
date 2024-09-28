package com.epam.esm.gym.workload.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeignConfigTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FeignConfig feignConfig;

    @Test
    void testErrorDecoderInternalServerError() {
        ErrorDecoder errorDecoder = feignConfig.errorDecoder();
        Response response = mock(Response.class);
        when(response.status()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Exception exception = errorDecoder.decode("methodKey", response);
        assertEquals(EntityNotFoundException.class, exception.getClass());
        assertEquals("An unexpected error occurred.", exception.getMessage());
    }

    @Test
    void testGetAccessTokenSuccess() {
        String expectedToken = "mocked_access_token";
        String loginUrl = "http://localhost:8090/api/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.ok(Map.of("access_token", expectedToken));
        when(restTemplate.exchange(
                eq(loginUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        String accessToken = feignConfig.getAccessToken();
        assertEquals(expectedToken, accessToken);
    }

    @Test
    void testGetAccessTokenHttpClientError() {
        String loginUrl = "http://localhost:8090/api/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        when(restTemplate.exchange(
                eq(loginUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        String accessToken = feignConfig.getAccessToken();
        assertEquals("", accessToken);
    }
}
