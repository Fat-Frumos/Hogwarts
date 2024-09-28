package com.epam.esm.gym.user.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for security settings in the test environment.
 *
 * <p>This class sets up a security filter chain that allows public access
 * to the login endpoints while requiring authentication for all other requests.</p>
 *
 * <p>CSRF protection is disabled for ease of testing.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigTestMock {

    /**
     * Configures the security filter chain.
     *
     * <p>This method specifies that the login and login change endpoints are
     * accessible without authentication, while all other requests require
     * the user to be authenticated.</p>
     *
     * @param http the HttpSecurity instance to configure security settings
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/login",
                                "/api/login/change").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
