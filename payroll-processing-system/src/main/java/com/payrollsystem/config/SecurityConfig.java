package com.payrollsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures security settings for the Payroll System web application.
 * This configuration enables Spring Security for the application, defining
 * specific HTTP security rules and behaviors.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines the default security filter chain for the application.
     * <p>
     * This method configures the application to permit all HTTP requests,
     * disables CSRF protection for simplicity in non-browser API interaction,
     * and configures default security headers to disable caching.
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.defaultsDisabled().cacheControl());

        return http.build();
    }

    /**
     * Configures security filter chain specifically for enabling Swagger and actuator URLs,
     * while securing all other endpoints.
     *
     * This configuration allows unauthenticated access to essential monitoring endpoints
     * and the Swagger UI for API documentation, while requiring authentication for all other
     * requests. It also sets up HTTP basic authentication with a specific realm.
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    /* just to enable the swagger and actuator URLs
        @Bean
        public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                            .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .httpBasic(httpBasic -> httpBasic.realmName("PayrollProcessingSystem"));
            return http.build();
        }
     */
}