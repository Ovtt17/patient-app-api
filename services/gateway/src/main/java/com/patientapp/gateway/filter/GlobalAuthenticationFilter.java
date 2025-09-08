package com.patientapp.gateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class GlobalAuthenticationFilter implements GlobalFilter {

    private final RouteValidator routeValidator;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = extractToken(request);

        if (token == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token no proporcionado"));
        }

        if (routeValidator.isSecured.test(request)) {
            try {
                validateToken(token);

                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            } catch (Exception e) {
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o expirado"));
            }
        }

        return chain.filter(exchange);
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // Si no hay token en header, revisar cookie "access_token"
        HttpCookie cookie = request.getCookies().getFirst("access_token");
        if (cookie != null) {
            return cookie.getValue();
        }

        return null;
    }

    private void validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}