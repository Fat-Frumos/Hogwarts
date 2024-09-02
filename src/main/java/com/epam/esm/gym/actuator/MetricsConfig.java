package com.epam.esm.gym.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class MetricsConfig {

    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void initCustomMetrics() {
        meterRegistry.gauge("custom_metric_one", 42);
        meterRegistry.counter("custom_metric_two").increment();
    }
}