package com.patientapp.doctorservice.modules.doctor.controller;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorPagedResponseDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("doctors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
@Tag(name = "Doctor", description = "Gestión de doctores")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Crear un nuevo doctor")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UUID> create(@RequestBody UUID userId) {
        var doctorId = doctorService.create(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorId);
    }

    @Operation(summary = "Obtener todos los doctores activos")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorPagedResponseDTO> getAllActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder
    ) {
        var doctors = doctorService.getAllActive(page, size, sortBy, sortOrder);
        if (doctors == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Obtener un doctor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getById(
            @Parameter(description = "UUID del doctor") @PathVariable UUID id
    ) {
        return ResponseEntity.ok(doctorService.getById(id));
    }

    @Operation(summary = "Obtener un doctor por ID de usuario")
    @GetMapping("/user/{userId}")
    public ResponseEntity<DoctorResponseDTO> getByUserId(
            @Parameter(description = "UUID del usuario") @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(doctorService.getByUserId(userId));
    }

    @Operation(summary = "Actualizar información de un doctor")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> update(
            @Parameter(description = "UUID del doctor") @PathVariable UUID id,
            @Valid @RequestBody DoctorRequestDTO request
    ) {
        return ResponseEntity.ok(doctorService.update(id, request));
    }

    @Operation(summary = "Desactivar un doctor")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deactivate(
            @Parameter(description = "UUID del doctor") @PathVariable UUID id
    ) {
        doctorService.deactivate(id);
        return ResponseEntity.ok("Doctor desactivado correctamente");
    }
}
