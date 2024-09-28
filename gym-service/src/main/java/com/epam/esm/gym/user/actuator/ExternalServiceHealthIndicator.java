package com.epam.esm.gym.user.actuator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Monitors the health status of an external service.
 *
 * <p>This class implements the {@code HealthIndicator} interface to check the
 * connectivity and responsiveness of an external service, such as third-party APIs
 * or microservices. It provides a health check report that integrates with the
 * overall application health monitoring system.</p>
 *
 * <p>Useful for ensuring that dependent services are operational and can be
 * accessed reliably.</p>
 *
 * @author Pavlo Poliak
 * @since 1.0
 */
@Slf4j
@Component
class ExternalServiceHealthIndicator implements HealthIndicator {

    @Value("${actuator.prometheus.url}")
    private String prometheusUrl;

    @Value("${actuator.info.url}")
    private String infoUrl;
    private static final String PROMETHEUS = "prometheus";
    private final RestTemplate restTemplate;

    ExternalServiceHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Provides the health status of Prometheus metrics and the Info endpoint.
     *
     * <p>This method overrides the {@link HealthIndicator#health()} method to assess
     * the availability of Prometheus metrics and the Info endpoint. It performs two checks:
     * whether Prometheus metrics are available and whether the Info endpoint is reachable.
     * The resulting health status reflects the combination of these checks, with
     * detailed messages indicating which services are up or down.</p>
     *
     * <p>If both services are available, it returns a {@link Health#up()} status with
     * detailed messages. If either service is unavailable, it returns a {@link Health#down()}
     * status with appropriate details for each failing service.</p>
     *
     * @return the combined health status of Prometheus metrics and the Info endpoint,
     * as {@link Health#up()} if both are reachable, otherwise {@link Health#down()}
     * with details about which services are unavailable.
     * @implSpec This method relies on {@code checkPrometheusMetrics()} and {@code checkInfoEndpoint()}
     * to determine the availability of the respective services.
     * @see #checkPrometheusMetrics()
     * @see #checkInfoEndpoint()
     * @see org.springframework.boot.actuate.health.Health
     * @see org.springframework.boot.actuate.health.HealthIndicator
     * @since 1.0
     */
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

    /**
     * Checks the availability of Prometheus metrics by sending a request to the Prometheus URL.
     *
     * <p>This method attempts to connect to the Prometheus metrics endpoint using a REST call
     * to the specified {@code PROMETHEUS_URL}. It checks if the response status is within the
     * 2xx successful range. If the connection is successful, it returns {@code true}, indicating
     * that the metrics are available. In the event of an exception, it returns {@code false},
     * indicating that the Prometheus metrics are not reachable.</p>
     *
     * @return {@code true} if the Prometheus metrics endpoint responds successfully;
     * {@code false} if it is unreachable or if an exception occurs.
     * This method is used internally by {@code health()} to determine Prometheus availability.
     * @see org.springframework.http.ResponseEntity
     * @since 1.0
     */
    private boolean checkPrometheusMetrics() {
        try {
            return restTemplate
                    .getForEntity(prometheusUrl, String.class)
                    .getStatusCode()
                    .is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks the availability of the Info endpoint by sending a request to the specified URL.
     *
     * <p>This method attempts to connect to the Info endpoint using a REST call to the
     * {@code INFO_URL}. It checks if the response status code is within the 2xx range,
     * indicating a successful connection. If the connection is successful, the method
     * returns {@code true}, confirming that the Info endpoint is reachable. If an exception
     * occurs during the request, the method returns {@code false}, indicating that the
     * Info endpoint is not accessible.</p>
     *
     * @return {@code true} if the Info endpoint responds successfully; {@code false} if it
     * is unreachable or if an exception occurs.
     * This method is used internally by {@code health()} to determine the
     * availability of the Info endpoint.
     * @see org.springframework.http.ResponseEntity
     * @since 1.0
     */
    private boolean checkInfoEndpoint() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(infoUrl, String.class);
            boolean isReachable = response.getStatusCode().is2xxSuccessful();
            log.info("Info endpoint response status: " + response.getStatusCode());
            return isReachable;
        } catch (Exception e) {
            log.info("Exception while checking Info endpoint: " + e.getMessage());
            return false;
        }
    }
}
