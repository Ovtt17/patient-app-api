package com.patientapp.patientservice.modules.patient.controller;

import com.patientapp.patientservice.modules.patient.dto.*;
import com.patientapp.patientservice.modules.patient.service.interfaces.PatientService;
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
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Paciente", description = "Gestión de pacientes")
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Crear un nuevo paciente")
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody PatientRequestDTO request) {
        var patientId = patientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientId);
    }

    @Operation(summary = "Obtener todos los pacientes activos con paginación")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<PatientPagedResponseDTO> getAllActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone
    ) {
        var patients = patientService.getAllActive(page, size, sortBy, sortOrder, name, email, phone);
        if (patients == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patients);
    }

    @Operation(summary = "Obtener un paciente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getById(
            @Parameter(description = "UUID del paciente")
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(patientService.getById(id));
    }

    @Operation(summary = "Obtener un paciente por ID de usuario")
    @GetMapping("/user/{userId}")
    public ResponseEntity<PatientResponseDTO> getByUserId(
            @Parameter(description = "UUID del usuario") @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(patientService.getByUserId(userId));
    }

    @PutMapping("/{userId}/basic-info")
    public ResponseEntity<Void> updateBasicInfo(
            @Parameter(description = "UUID del paciente")
            @PathVariable UUID userId,
            @Valid @RequestBody PatientBasicInfoDTO request
    ) {
        patientService.updateBasicInfo(userId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar información médica del paciente")
    @PutMapping("/{userId}/medical-info")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<PatientResponseDTO> updateMedicalInfo(
            @Parameter(description = "UUID del usuario") @PathVariable UUID userId,
            @Valid @RequestBody PatientMedicalInfoDTO request
    ) {
        return ResponseEntity.ok(patientService.updateMedicalInfo(userId, request));
    }

    @Operation(summary = "Desactivar un paciente")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<?> deactivate(
            @Parameter(description = "UUID del paciente") @PathVariable UUID id
    ) {
        patientService.deactivate(id);
        return ResponseEntity.ok("Paciente desactivado correctamente");
    }

    @Operation(summary = "Obtener pacientes por una lista de IDs")
    @PostMapping("/by-ids")
    public ResponseEntity<List<PatientResponseDTO>> getByIds(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(patientService.getByIds(ids));
    }

    @GetMapping("/count-all")
    public ResponseEntity<Long> countAll() {
        return ResponseEntity.ok(patientService.countAll());
    }
}
