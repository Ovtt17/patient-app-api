package com.patientapp.doctorservice.doctor.controller;

import com.patientapp.doctorservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.doctor.service.interfaces.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("doctors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Crear un nuevo doctor")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<UUID> create(
            @Valid @RequestBody DoctorRequestDTO request
    ) {
        var doctorId = doctorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorId);
    }

    @Operation(summary = "Obtener todos los doctores activos")
    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllActive() {
        return ResponseEntity.ok(doctorService.getAllActive());
    }

    @Operation(summary = "Obtener un doctor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getById(
            @Parameter(description = "UUID del doctor") @PathVariable UUID id
    ) {
        return ResponseEntity.ok(doctorService.getById(id));
    }

    @Operation(summary = "Actualizar información de un doctor")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DoctorResponseDTO> update(
            @Parameter(description = "UUID del doctor") @PathVariable UUID id,
            @Valid @RequestBody DoctorRequestDTO request
    ) {
        return ResponseEntity.ok(doctorService.update(id, request));
    }

    @Operation(summary = "Desactivar un doctor")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deactivate(
            @Parameter(description = "UUID del doctor") @PathVariable UUID id
    ) {
        doctorService.deactivate(id);
        return ResponseEntity.ok("Doctor desactivado correctamente");
    }
}
