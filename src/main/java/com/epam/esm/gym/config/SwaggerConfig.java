package com.epam.esm.gym.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Swagger for API documentation in the Spring Boot application.
 *
 * <p>This class sets up Swagger to generate interactive API documentation
 * based on the application's REST endpoints, allowing developers to easily
 * understand and test API functionalities.</p>
 *
 * <p>Includes customization of API metadata and available endpoints.</p>
 *
 * @author Pavlo Poliak
 * @since 1.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates a default OpenAPI configuration for the application.
     *
     * <p>This method configures the OpenAPI documentation for the backend with security requirements and information.
     * It sets up the security scheme for bearer authentication, including JWT,
     * and provides basic information such as the title, description, version, and contact details for the API.
     * The security scheme is specified as "Bearer Authentication" which is used to authenticate API requests.
     * This configuration is typically used to generate and display API documentation.</p>
     *
     * @return an {@link OpenAPI} instance configured with security requirements and API information
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("Gym")
                        .description("The official backend OpenAPI Spec.")
                        .version("1.0").contact(new Contact().name("Paul Poliak")));
    }

    /**
     * Customizes the OpenAPI configuration for bearer authentication.
     *
     * <p>This method defines an alternative OpenAPI configuration focusing on the "bearerAuth" security scheme.
     * It configures the OpenAPI to use bearer authentication with JWT and specifies the scheme details.
     * This customized configuration is useful for scenarios where different or additional security requirements
     * are needed. It overrides default settings to accommodate specific security needs or requirements.</p>
     *
     * @return an {@link OpenAPI} instance customized with bearer authentication scheme details
     */
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    /**
     * Creates a security scheme for bearer authentication using JWT.
     *
     * <p>This method configures a {@link SecurityScheme} for HTTP bearer authentication with JWT format.
     * It sets the scheme type to HTTP and specifies the authentication method as "bearer" with JWT.
     * This configuration is used to define how security is handled in the OpenAPI specification,
     * ensuring that the API documentation includes the correct authentication requirements.
     * The resulting {@link SecurityScheme} can be used to secure API endpoints.</p>
     *
     * @return a {@link SecurityScheme} instance configured for bearer authentication with JWT
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
