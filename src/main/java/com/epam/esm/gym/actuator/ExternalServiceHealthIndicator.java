package com.epam.esm.gym.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ExternalServiceHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        boolean serviceIsUp = checkService();
        return serviceIsUp ? Health.up().withDetail("externalService", "Available").build()
                : Health.down().withDetail("externalService", "Not Reachable").build();
    }

    private boolean checkService() {
        return true;
    }
}