package com.patientapp.authservice.modules.auth.controller;

import com.patientapp.authservice.modules.auth.dto.ChangePasswordRequest;
import com.patientapp.authservice.modules.auth.dto.LoginRequest;
import com.patientapp.authservice.modules.auth.dto.RegisterRequest;
import com.patientapp.authservice.modules.auth.service.interfaces.AuthService;
import com.patientapp.authservice.modules.user.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints para autenticación y gestión de usuarios")
public class AuthController {
    private final AuthService authService;

    /**
     * Registers a new user in the system.
     * @param request The registration request containing user details.
     * @return A message indicating successful registration.
     */
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario (paciente o doctor) con contraseña temporal y obligación de cambio en el primer inicio de sesión.")
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> register(
            @RequestBody @Valid final RegisterRequest request
    ) {
        final String message = authService.register(request);
        return ResponseEntity.accepted().body(message);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletResponse response
    ) {
        authService.changePassword(request, response);
        return ResponseEntity.ok("Contraseña cambiada exitosamente.");
    }

    /**
     * Activates a user account using the provided activation token.
     * @param token The activation token sent to the user's email.
     * @return A message indicating successful account activation.
     */
    @Operation(summary = "Activar cuenta de usuario", description = "Activa una cuenta de usuario usando el token de activación proporcionado.")
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
    @Operation(summary = "Iniciar sesión de usuario", description = "Autentica a un usuario y retorna sus detalles. Establece cookies de autenticación en la respuesta.")
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
    @Operation(summary = "Obtener usuario actual", description = "Retorna los detalles del usuario autenticado usando los tokens de acceso y refresco.")
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
    @Operation(summary = "Refrescar token de acceso", description = "Refresca el token de acceso usando el token de refresco proporcionado y establece una nueva cookie de acceso.")
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
    @Operation(summary = "Cerrar sesión de usuario", description = "Cierra la sesión del usuario actual eliminando las cookies de autenticación.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.logout(response));
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        boolean valid = authService.validateToken(token);
        if (!valid) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        return ResponseEntity.ok("Token válido");
    }
}