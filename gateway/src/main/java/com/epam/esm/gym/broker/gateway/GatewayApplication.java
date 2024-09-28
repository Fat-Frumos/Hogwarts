package com.epam.esm.gym.broker.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Gateway application.
 * This class contains the main method that starts the Spring Boot application
 * for the Gateway service. The application is initialized with default settings
 * and configurations defined in the classpath.
 */
@SpringBootApplication
public class GatewayApplication {
    /**
     * Main method to run the Spring Boot application.
     * This method is the entry point for the application. It launches the Spring Boot
     * application by calling {@link SpringApplication#run(Class, String...)} with the
     * {@link GatewayApplication} class and any provided command-line arguments.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
