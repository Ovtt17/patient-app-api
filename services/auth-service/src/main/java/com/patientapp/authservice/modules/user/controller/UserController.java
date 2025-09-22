package com.patientapp.authservice.modules.user.controller;

import com.patientapp.authservice.modules.user.dto.UserRequestDTO;
import com.patientapp.authservice.modules.user.dto.UserResponseDTO;
import com.patientapp.authservice.modules.user.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints para gestión de usuarios")
public class UserController {
    private final UserService service;

    @Operation(summary = "Obtener usuario por ID", description = "Obtiene los detalles de un usuario específico mediante su ID.")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getUserById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> update(
            @Parameter(description = "ID del usuario a actualizar", required = true)
            @PathVariable UUID userId,
            @Valid @RequestBody UserRequestDTO request
    ) {
        return ResponseEntity.ok(service.update(userId, request));
    }
}
