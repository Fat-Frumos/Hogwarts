package com.epam.esm.gym.jms;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Main entry point for the JmsApplication application.
 * This class contains the main method that starts the Spring Boot application
 * for the Jms service. The application is initialized with default settings
 * and configurations defined in the classpath.
 */
@Slf4j
public class JmsApplication {
    /**
     * Main method to run the Spring Boot application.
     * This method is the entry point for the application.
     * {@link JmsApplication} class and any provided command-line arguments.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        log.info(Arrays.toString(args));
    }
}
