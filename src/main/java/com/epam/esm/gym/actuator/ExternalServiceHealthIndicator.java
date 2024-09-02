package com.epam.esm.gym.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class ExternalServiceHealthIndicator implements HealthIndicator {
    private static final String PROMETHEUS_URL = "http://localhost:8080/actuator/prometheus";
    private static final String INFO_URL = "http://localhost:8080/actuator/info";
    private static final String PROMETHEUS = "prometheus";

    private final RestTemplate restTemplate;

    public ExternalServiceHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        boolean prometheusUp = checkPrometheusMetrics();
        boolean infoUp = checkInfoEndpoint();


        if (prometheusUp && infoUp) {
            return Health.up().withDetail(PROMETHEUS, "Metrics available")
                    .withDetail("info", "Info endpoint available").build();
        } else if (prometheusUp) {
            return Health.down().withDetail("info", "Info endpoint not reachable").build();
        } else if (infoUp) {
            return Health.down().withDetail(PROMETHEUS, "Prometheus metrics not reachable").build();
        } else {
            return Health.down().withDetail(PROMETHEUS, "Prometheus metrics not reachable")
                    .withDetail("info", "Info endpoint not reachable").build();
        }
    }

    private boolean checkPrometheusMetrics() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(PROMETHEUS_URL, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkInfoEndpoint() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(INFO_URL, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
