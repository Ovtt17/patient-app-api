package com.patientapp.doctorservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JwtUtils {

    /**
     * Obtiene el userId del JWT actual si está autenticado.
     *
     * @return Optional con el UUID del userId, o vacío si no está presente.
     */
    public Optional<UUID> getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof Jwt jwt) {
            Object userIdClaim = jwt.getClaim("userId");
            if (userIdClaim != null) {
                try {
                    return Optional.of(UUID.fromString(userIdClaim.toString()));
                } catch (IllegalArgumentException e) {
                    // En caso de que no sea UUID válido
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Versión que lanza excepción si no hay usuario autenticado o el claim no existe.
     */
    public UUID getUserIdOrThrow() {
        return getUserIdFromToken()
                .orElseThrow(() -> new IllegalStateException("No se pudo obtener el userId del token JWT"));
    }
}