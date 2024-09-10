package com.epam.esm.gym.config;

import com.epam.esm.gym.security.filter.BruteForceProtectionFilter;
import com.epam.esm.gym.security.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configures security settings for the application.
 *
 * <p>This class sets up security rules, including authentication and authorization
 * requirements, security filters, and access control policies. It ensures that
 * the application meets its security standards and protects resources appropriately.</p>
 *
 * @author Pavlo Poliak
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final BruteForceProtectionFilter bruteForceProtectionFilter;

    /**
     * Configures the security filter chain for the application, specifying security
     * rules, authentication, and session management.
     *
     * <p>This configuration sets up various HTTP security aspects, including CORS settings,
     * authorization rules, form-based login, logout handling, exception management, and session
     * policies. The security rules include permitting access to specific endpoints for public use,
     * restricting access based on roles, and integrating custom filters for JWT and brute force protection.</p>
     *
     * @param http the {@link HttpSecurity} object used to configure the security of the application.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if there is an error configuring the security settings.
     *                   Disables CSRF protection and sets the session management policy stateless to
     *                   accommodate RESTful API security needs.
     * @see org.springframework.security.config.annotation.web.builders.HttpSecurity
     * @see org.springframework.security.web.SecurityFilterChain
     * @since 1.0
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/actuator/**",
                                "/api/trainers/register",
                                "/api/trainees/register",
                                "/api/login",
                                "/api/login/change",
                                "/api/auth/login",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/css/**",
                                "/js/**",
                                "/index.html"
                        ).permitAll()
//                        .requestMatchers("/api/trainees/**").hasAnyRole(TRAINEE, TRAINER, ADMIN)
//                        .requestMatchers("/api/trainers/**").hasAnyRole(TRAINER, ADMIN)
//                        .anyRequest().hasRole(ADMIN))
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403")
                        .authenticationEntryPoint(unauthorizedHandler())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(bruteForceProtectionFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Provides a {@link PasswordEncoder} bean for encoding passwords.
     *
     * <p>This method returns a {@link BCryptPasswordEncoder}, which uses the BCrypt
     * hashing algorithm to securely encode user passwords.</p>
     *
     * @return the configured {@link PasswordEncoder}.
     * Password encoding is critical for ensuring that user passwords are stored securely.
     * @see org.springframework.security.crypto.password.PasswordEncoder
     * @since 1.0
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Configures CORS settings for the application.
     *
     * <p>This method creates and configures a {@link CorsConfigurationSource} to allow
     * cross-origin requests from specified origins, methods, and headers. The CORS
     * configuration is applied globally to all endpoints.</p>
     *
     * @return the configured {@link CorsConfigurationSource}.
     * Proper CORS configuration is necessary to allow the frontend to interact
     * with the backend securely.
     * @see org.springframework.web.cors.CorsConfigurationSource
     * @since 1.0
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Provides an {@link AuthenticationManager} bean for managing authentication.
     *
     * <p>This method creates and configures an {@link AuthenticationManager} using the
     * provided {@link AuthenticationConfiguration}. The {@link AuthenticationManager}
     * is responsible for authenticating user credentials.</p>
     *
     * @param authConfig the {@link AuthenticationConfiguration} to configure the
     *                   {@link AuthenticationManager}.
     * @return the configured {@link AuthenticationManager}.
     * @throws Exception if an error occurs during the configuration process.
     *                   This method is essential for setting up the authentication mechanism in the application.
     * @see org.springframework.security.authentication.AuthenticationManager
     * @since 1.0
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Provides a custom {@link AuthenticationEntryPoint} that handles unauthorized access attempts.
     *
     * <p>This method creates an {@link AuthenticationEntryPoint} that sends an HTTP 401
     * Unauthorized response when an unauthorized request is detected.</p>
     *
     * @return the configured {@link AuthenticationEntryPoint}.
     * This handler ensures that unauthorized access attempts are properly handled.
     * @see org.springframework.security.web.AuthenticationEntryPoint
     * @since 1.0
     */
    @Bean
    public AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
