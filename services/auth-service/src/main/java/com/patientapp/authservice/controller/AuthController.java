package com.patientapp.authservice.controller;

import com.patientapp.authservice.doctor.dto.DoctorCreatedDTO;
import com.patientapp.authservice.doctor.dto.DoctorRequestDTO;
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
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario con los detalles proporcionados.")
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid final RegisterRequest request
    ) {
        final String message = authService.register(request);
        return ResponseEntity.accepted().body(message);
    }

    /**
     * Registers a new doctor in the system and generates a user with DOCTOR role and a temporary password.
     * @param request The doctor registration request containing doctor details.
     * @return The created doctor's details including temporary password.
     */
    @Operation(summary = "Registrar nuevo doctor", description = "Registra un nuevo doctor en el sistema. Se genera un usuario con rol DOCTOR y una contraseña temporal.")
    @PostMapping("/register-doctor")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorCreatedDTO> registerDoctor(
            @RequestBody @Valid final DoctorRequestDTO request
    ) {
        return ResponseEntity.ok(authService.registerDoctor(request));
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
}