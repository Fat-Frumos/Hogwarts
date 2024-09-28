package com.epam.esm.gym.user.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * Configures metrics for monitoring application performance.
 *
 * <p>This class sets up the necessary configurations for collecting and exposing
 * application metrics. It may include settings for integrating with monitoring
 * tools, such as Prometheus or other observability platforms, to track key
 * performance indicators.</p>
 *
 * <p>Metrics collected can help in understanding application behavior, performance
 * bottlenecks, and resource utilization.</p>
 *
 * <p>The configuration is applied globally within the Spring Boot application.</p>
 *
 * @author Pavlo Poliak
 * @see org.springframework.context.annotation.Configuration
 * @see lombok.AllArgsConstructor
 * @since 1.0
 */
@Configuration
@AllArgsConstructor
public class MetricsConfig {

    /**
     * The {@link MeterRegistry} instance used to register custom metrics for the application.
     *
     * <p>This registry facilitates the creation, management, and reporting of various metrics,
     * including gauges, counters, timers, and more. It integrates with different monitoring
     * systems, allowing the application to provide performance and usage data in a structured
     * and accessible manner.</p>
     *
     * @see io.micrometer.core.instrument.MeterRegistry
     * @since 1.0
     */
    private final MeterRegistry meterRegistry;

    /**
     * Initializes custom metrics and registers them with the {@link MeterRegistry}.
     *
     * <p>This method is annotated with {@code @PostConstruct} to ensure that it is executed
     * immediately after the bean's properties are set and the bean itself is initialized.
     * It registers two custom metrics: a gauge and a counter. The gauge metric, named
     * {@code custom_metric_one}, is initialized with a static value of 42. The counter metric,
     * {@code custom_metric_two}, is incremented upon initialization, which can be used to track
     * specific events or occurrences in the application.</p>
     *
     * <p>The use of the {@link MeterRegistry} allows for seamless integration with various monitoring
     * systems, providing real-time insights into application performance and behavior.</p>
     * This method relies on {@link MeterRegistry} for metric registration and management.
     *
     * @see io.micrometer.core.instrument.MeterRegistry
     * @see jakarta.annotation.PostConstruct
     * @since 1.0
     */
    @PostConstruct
    public void initCustomMetrics() {
        meterRegistry.gauge("custom_metric_one", 42);
        meterRegistry.counter("custom_metric_two").increment();
    }
}
