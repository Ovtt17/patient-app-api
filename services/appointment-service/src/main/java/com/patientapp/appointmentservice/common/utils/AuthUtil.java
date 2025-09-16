package com.patientapp.appointmentservice.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthUtil {
    private AuthUtil() {}

    public static String getUserIdFromJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Object userId = jwtAuth.getToken().getClaim("userId");
            return userId != null ? userId.toString() : null;
        }
        return null;
    }
}

