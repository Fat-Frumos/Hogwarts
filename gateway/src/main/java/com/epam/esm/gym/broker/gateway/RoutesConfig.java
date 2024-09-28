package com.epam.esm.gym.broker.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {

    @Bean
    public RouteLocator userServiceRoute(
            RouteLocatorBuilder builder,
            AuthenticationGateWayFilter authenticationGateWayFilter) {
        return builder.routes()
                .route("user-service", spec -> spec.path("/api/users/**")
                        .filters(f -> f.filter(authenticationGateWayFilter))
                        .uri("lb://USER-SERVICE"))
                .route("training-service", spec -> spec.path("/api/trainings/**")
                        .uri("lb://TRAINING-SERVICE"))
                .route("workload-service", spec -> spec.path("/api/workload/**")
                        .uri("lb://WORKLOAD-SERVICE"))
                .route("trainer-service", spec -> spec.path("/api/trainers/**")
                        .uri("lb://TRAINER-SERVICE"))
                .route("trainee-service", spec -> spec.path("/api/trainees/**")
                        .uri("lb://TRAINEE-SERVICE"))
                .build();
    }
}
