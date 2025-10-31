package com.patientapp.doctorservice.modules.doctor.controller;

import com.patientapp.doctorservice.modules.doctor.dto.*;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor", description = "Gestión de doctores")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Crear un nuevo doctor")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UUID> create(@RequestBody DoctorRequestDTO request) {
        var doctorId = doctorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorId);
    }

    @Operation(summary = "Obtener todos los doctores activos")
    @GetMapping("/all")
    public ResponseEntity<List<DoctorResponseDTO>> getAllActive() {
        var doctors = doctorService.getAllActive();
        if (doctors == null || doctors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Obtener todos los doctores activos")
    @GetMapping("/paged")
    public ResponseEntity<DoctorPagedResponseDTO> getAllActivePaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder
    ) {
        var doctors = doctorService.getAllActivePaged(page, size, sortBy, sortOrder);
        if (doctors == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Obtener un doctor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getById(
            @Parameter(description = "UUID del doctor")
            @PathVariable UUID id
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

    @PutMapping("/{userId}/basic-info")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<Void> updateBasicInfo(
            @Parameter(description = "UUID del usuario")
            @PathVariable UUID userId,
            @Valid @RequestBody DoctorBasicInfoDTO request
    ) {
        doctorService.updateBasicInfo(userId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar información médica del doctor")
    @PutMapping("/{userId}/medical-info")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<DoctorResponseDTO> updateMedicalInfo(
            @Parameter(description = "UUID del usuario")
            @PathVariable UUID userId,
            @Valid @RequestBody DoctorMedicalInfoDTO request
    ) {
        return ResponseEntity.ok(doctorService.updateMedicalInfo(userId, request));
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

    @PostMapping("/by-ids")
    ResponseEntity<List<DoctorResponseDTO>> getByIds(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(doctorService.getByIds(ids));
    }
}
