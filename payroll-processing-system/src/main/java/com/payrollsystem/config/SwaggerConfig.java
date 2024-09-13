package com.payrollsystem.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Swagger for API documentation.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures API grouping and documentation paths.
     *
     * @return Configured GroupedOpenApi
     */
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("com.payrollsystem.controller")
                .pathsToMatch("/api/**")
                .build();
    }
}
