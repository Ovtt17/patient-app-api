package com.patientapp.authservice.controller;

import com.patientapp.authservice.dto.LoginRequest;
import com.patientapp.authservice.dto.RegisterRequest;
import com.patientapp.authservice.dto.UserResponseDTO;
import com.patientapp.authservice.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints for authentication and user management")
public class AuthController {
    private final AuthService authService;

    /**
     * Registers a new user in the system.
     * @param request The registration request containing user details.
     * @return A message indicating successful registration.
     */
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid final RegisterRequest request
    ) {
        final String message = authService.register(request);
        return ResponseEntity.accepted().body(message);
    }

    /**
     * Activates a user account using the provided activation token.
     * @param token The activation token sent to the user's email.
     * @return A message indicating successful account activation.
     */
    @Operation(summary = "Activate user account", description = "Activates a user account using the provided activation token.")
    @PostMapping("/activate-account")
    public ResponseEntity<String> activateAccount(
            @RequestParam final String token
    ) {
        final String message = authService.activateAccount(token);
        return ResponseEntity.ok().body(message);
    }

    /**
     * Authenticates a user and returns user details with tokens in cookies.
     * @param request The login request containing username/email and password.
     * @param response The HTTP response to set cookies.
     * @return The authenticated user's details.
     */
    @Operation(summary = "User login", description = "Authenticates a user and returns user details. Sets authentication cookies in the response.")
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(
            @RequestBody @Valid final LoginRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    /**
     * Returns the currently authenticated user's details.
     * @param accessToken The access token from the cookie.
     * @param refreshToken The refresh token from the cookie.
     * @param response The HTTP response to set cookies if needed.
     * @return The current user's details.
     */
    @Operation(summary = "Get current user", description = "Returns the currently authenticated user's details using the access and refresh tokens.")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
            @CookieValue(name = "access_token", required = false) final String accessToken,
            @CookieValue(name = "refresh_token", required = false) final String refreshToken,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.getCurrentUser(accessToken, refreshToken, response));
    }

    /**
     * Refreshes the access token using the provided refresh token.
     * @param refreshToken The refresh token from the cookie.
     * @param response The HTTP response to set the new access token cookie.
     * @return A message indicating the access token was refreshed.
     */
    @Operation(summary = "Refresh access token", description = "Refreshes the access token using the provided refresh token and sets a new access token cookie.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(name = "refresh_token", required = false) final String refreshToken,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.refresh(refreshToken, response));
    }

    /**
     * Logs out the current user by clearing authentication cookies.
     * @param response The HTTP response to clear cookies.
     * @return A message indicating successful logout.
     */
    @Operation(summary = "Logout user", description = "Logs out the current user by clearing authentication cookies.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.logout(response));
    }
}
