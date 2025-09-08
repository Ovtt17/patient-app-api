package com.patientapp.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // Public endpoints that do NOT require authentication
    private static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/activate-account",
            "/api/v1/auth/refresh",
            "/api/v1/auth/logout",
            "/api/v1/oauth2/authorization/google",
            "/api/v1/oauth2/authorization/facebook",
            "/api/v1/login/oauth2/code/google",
            "/api/v1/login/oauth2/code/facebook",
            "/api/v1/csrf/token",
            "/api/v1/v3/api-docs",
            "/api/v1/swagger-ui",
            "/api/v1/swagger-resources",
            "/api/v1/webjars"
    );

    // Predicate: if the route is NOT in the list of public endpoints → requires authentication
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().startsWith(uri));

    // Optional helper method to check if the route is public
    public boolean isPublic(ServerHttpRequest request) {
        return openApiEndpoints.stream()
                .anyMatch(uri -> request.getURI().getPath().startsWith(uri));
    }
}
