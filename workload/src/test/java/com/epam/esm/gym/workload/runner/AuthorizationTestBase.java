package com.epam.esm.gym.workload.runner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

/**
 * Base class for authorization-related tests.
 * This class retrieves an access token before all tests that require authentication.
 */
public class AuthorizationTestBase extends TestBase {

    protected static String accessToken;
    protected static String username;

    /**
     * Retrieves the access token from the authentication service.
     */
    @BeforeAll
    public static void getToken() {
        String loginUrl = RestAssured.baseURI + "/api/auth/login";

        Map<String, Object> admin = Map.of(
                "username", "Lord.Voldemort",
                "password", "You-Know-Who"
        );

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .body(admin)
                .post(loginUrl)
                .then()
                .statusCode(200)
                .extract()
                .response();

        accessToken = response.jsonPath().getString("accessToken");
        username = response.jsonPath().getString("username");
    }

    /**
     * Returns a RequestSpecification with authorization headers.
     *
     * @return the configured RequestSpecification
     */
    protected RequestSpecification getAuthorization() {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken);
    }
}
