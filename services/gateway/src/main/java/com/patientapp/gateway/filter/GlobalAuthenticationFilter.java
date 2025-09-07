package com.patientapp.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GlobalAuthenticationFilter implements GlobalFilter {

    private final RouteValidator routeValidator;
    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = null;

        if (routeValidator.isSecured.test(request)) {

            // Revisar header Authorization
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            // Revisar cookie si no hay token
            if (token == null) {
                HttpCookie cookie = request.getCookies().getFirst("access_token");
                if (cookie != null) token = cookie.getValue();
            }

            if (token == null) {
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token no proporcionado"));
            }

            String finalToken = token;
            return webClientBuilder.build()
                    .get()
                    .uri("lb://AUTH-SERVICE/api/v1/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + finalToken)
                    .exchangeToMono(resp -> {
                        if (resp.statusCode().equals(HttpStatus.OK)) {
                            return chain.filter(exchange.mutate()
                                    .request(exchange.getRequest().mutate()
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + finalToken)
                                            .build())
                                    .build());
                        } else {
                            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o expirado"));
                        }
                    });
        }

        return chain.filter(exchange);
    }
}