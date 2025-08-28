package com.patientapp.authservice.common.utils;

import com.patientapp.authservice.modules.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {

    /**
     * Get the current authentication object from the security context.
     * @return An Optional containing the Authentication object if present, otherwise an empty Optional.
     */
    public static Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Get the email (username) of the currently authenticated user.
     * @return An Optional containing the email if the user is authenticated, otherwise an empty Optional.
     */
    public static Optional<String> getAuthenticatedEmail() {
        return getAuthentication()
                .map(Authentication::getName);
    }

    /**
     * Get the currently authenticated User object.
     * @return An Optional containing the User object if the user is authenticated, otherwise an empty Optional.
     */
    public static Optional<User> getAuthenticatedUser() {
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof User)
                .map(User.class::cast);
    }
}
