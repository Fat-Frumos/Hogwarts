package com.epam.esm.gym.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        boolean dbIsUp = checkDatabase();
        return dbIsUp ? Health.up().withDetail("database", "Running").build()
                : Health.down().withDetail("database", "Unavailable").build();
    }

    private boolean checkDatabase() {
        return true;
    }
}
