package com.epam.esm.gym.user.actuator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.actuate.health.Health.down;

/**
 * Unit tests for {@link ExternalServiceHealthIndicator}.
 * This class contains test cases that validate the behavior of the
 * {@link ExternalServiceHealthIndicator}, particularly focusing on the
 * health checks for external services like Prometheus and the Info endpoint.
 */
@ExtendWith(MockitoExtension.class)
public class ExternalServiceHealthIndicatorTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ExternalServiceHealthIndicator healthIndicator;

    private final String prometheusUrl = "http://localhost:9090/metrics";
    private final String infoUrl = "http://localhost:8080/info";

    @BeforeEach
    void setUp() throws Exception {
        healthIndicator = new ExternalServiceHealthIndicator(restTemplate);
        setField(healthIndicator, "prometheusUrl", prometheusUrl);
        setField(healthIndicator, "infoUrl", infoUrl);
    }

    private void setField(Object targetObject, String fieldName, Object fieldValue) throws Exception {
        Field field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(targetObject, fieldValue);
    }

    @Test
    void whenPrometheusIsDownThenHealthIsDown() {
        when(restTemplate.getForEntity(prometheusUrl, String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));
        when(restTemplate.getForEntity(infoUrl, String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Health health = healthIndicator.health();
        assertEquals(Health.down().withDetail("prometheus", "Prometheus metrics not reachable").build(), health);
    }

    @Test
    void whenInfoEndpointIsDownThenHealthIsDown() {
        when(restTemplate.getForEntity(prometheusUrl, String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(restTemplate.getForEntity(infoUrl, String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));
        Health health = healthIndicator.health();
        assertEquals(Health.down().withDetail("info", "Info endpoint not reachable").build(), health);
    }

    @Test
    void whenBothServicesAreDownThenHealthIsDown() {
        when(restTemplate.getForEntity(prometheusUrl, String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));
        when(restTemplate.getForEntity(infoUrl, String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));
        Health health = healthIndicator.health();
        assertEquals(Health.down().withDetail("prometheus", "Prometheus metrics not reachable")
                .withDetail("info", "Info endpoint not reachable").build(), health);
    }

    @Test
    void whenBothServicesAreUpThenHealthIsUp() {
        when(restTemplate.getForEntity(prometheusUrl, String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(restTemplate.getForEntity(infoUrl, String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Health health = healthIndicator.health();
        assertEquals(Health.up().withDetail("prometheus", "Metrics available")
                .withDetail("info", "Info endpoint available").build(), health);
    }

    @Test
    void testHealthBothDown() {
        when(restTemplate.getForEntity("http://prometheus.url", String.class))
                .thenThrow(new RuntimeException("Prometheus down"));
        when(restTemplate.getForEntity("http://info.url", String.class))
                .thenThrow(new RuntimeException("Info endpoint down"));
        Health health = healthIndicator.health();
        assertEquals(down().withDetail("prometheus", "Prometheus metrics not reachable")
                .withDetail("info", "Info endpoint not reachable").build(), health);
    }
}
