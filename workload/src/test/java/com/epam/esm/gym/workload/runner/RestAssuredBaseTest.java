package com.epam.esm.gym.workload.runner;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Base class for all tests that utilize RestAssured.
 * This class serves as a foundation for executing tests with RestAssured for API testing.
 */
@ExtendWith(RunnerExtension.class)
public abstract class RestAssuredBaseTest {

    /**
     * Set up the necessary configurations and prerequisites for RestAssured tests.
     * This method can be overridden by subclasses to provide specific configurations.
     */

    @BeforeAll
    public static void setup() {
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(LogDetail.ALL),
                new ResponseLoggingFilter(LogDetail.ALL)
        );
    }
}
