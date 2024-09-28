package com.epam.esm.gym.workload.runner;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Base class for configuring REST-assured tests.
 * This class sets the base URI and base path for API requests.
 */
@Configuration
@Profile("test")
@ExtendWith(RunnerExtension.class)
@ConfigurationProperties(prefix = "server")
public class TestBase {

    public static boolean isTestSuccessful;

    private static String baseUrl;
    private static String baseHost;

    /**
     * Sets up the BaseUrl each test.
     * This method can be overridden by subclasses for specific setup needs.
     */
    @Value("${server.local.base:http://localhost:8090/api/auth/login}")
    public void setBaseUrl(String baseUrl) {
        TestBase.baseUrl = baseUrl;
    }

    /**
     * Sets up the baseHost each test.
     * This method can be overridden by subclasses for specific setup needs.
     */
    @Value("${server.remote.base:https://hogwart-gym.onrender.com}")
    public void setBaseHost(String baseHost) {
        TestBase.baseHost = baseHost;
    }

    /**
     * Sets up the base URI and base path for REST-assured before all tests.
     */
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = baseHost;
        RestAssured.basePath = baseUrl;
    }
}
