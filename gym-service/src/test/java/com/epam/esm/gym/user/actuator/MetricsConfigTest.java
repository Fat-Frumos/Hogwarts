package com.epam.esm.gym.user.actuator;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@SpringBootTest
class MetricsConfigTest {

    private SimpleMeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        new MetricsConfig(meterRegistry).initCustomMetrics();
    }

    @Test
    void testCustomMetrics() {
        assertEquals(42, Objects.requireNonNull(
                meterRegistry.find("custom_metric_one").gauge()).value());
        assertEquals(1, Objects.requireNonNull(
                meterRegistry.find("custom_metric_two").counter()).count());
    }

    @Configuration
    static class TestConfig {
        @Bean
        public SimpleMeterRegistry simpleMeterRegistry() {
            return new SimpleMeterRegistry();
        }
    }
}
