package com.bankingsystem.apigateway.config;

import com.bankingsystem.apigateway.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8081"))

                // Account Service Routes
                .route("account-service", r -> r.path("/api/accounts/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8082"))

                // Transaction Service Routes
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8083"))

                // Loan Service Routes
//                .route("loan-service", r -> r.path("/api/loans/**")
//                        .filters(f -> f.stripPrefix(1)
//                                .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:8084"))

                // Notification Service Routes
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8084"))
                .build();
    }
}
