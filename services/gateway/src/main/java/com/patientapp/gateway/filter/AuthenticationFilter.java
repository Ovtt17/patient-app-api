package com.patientapp.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;
    private final WebClient.Builder webClientBuilder;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            String token = null;

            // Si la ruta es segura, validamos el token
            if (routeValidator.isSecured.test(request)) {

                // Primero revisa Authorization header
                if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        token = authHeader.substring(7);
                    }
                }

                // Si no hay header, intenta con cookie
                if (token == null) {
                    HttpCookie cookie = request.getCookies().getFirst("access_token");
                    if (cookie != null) {
                        token = cookie.getValue();
                    }
                }

                // Si todavía no hay token → error
                if (token == null) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
                }

                // Llamamos al auth-service para validar el token
                return webClientBuilder.build()
                        .get()
                        .uri("http://auth-service/api/v1/auth/validate?token=" + token)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, response -> {
                            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
                        })
                        .toBodilessEntity()
                        .flatMap(response -> chain.filter(exchange));
            }

            // Si la ruta es pública → continúa normalmente
            return chain.filter(exchange);
        };
    }

    // Clase de configuración requerida por AbstractGatewayFilterFactory
    public static class Config {
    }
}
