package com.patientapp.gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            // Optionally log the error here
            return false;
        }
    }

    public String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(org.springframework.http.HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        org.springframework.http.HttpCookie cookie = request.getCookies().getFirst("access_token");
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }
}
