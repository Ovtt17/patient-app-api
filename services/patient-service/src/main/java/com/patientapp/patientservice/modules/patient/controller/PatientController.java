package com.patientapp.patientservice.modules.patient.controller;

import com.patientapp.patientservice.modules.patient.dto.PatientPagedResponseDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientRequestDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientResponseDTO;
import com.patientapp.patientservice.modules.patient.service.interfaces.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("patients")
@RequiredArgsConstructor
@Tag(name = "Paciente", description = "Gestión de pacientes")
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Crear un nuevo paciente")
    @PostMapping
    @Transactional
    public ResponseEntity<UUID> create(@RequestBody UUID userId) {
        var patientId = patientService.create(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientId);
    }

    @Operation(summary = "Obtener todos los pacientes activos con paginación")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<PatientPagedResponseDTO> getAllActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder
    ) {
        var patients = patientService.getAllActive(page, size, sortBy, sortOrder);
        if (patients == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patients);
    }

    @Operation(summary = "Obtener un paciente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getById(
            @Parameter(description = "UUID del paciente") @PathVariable UUID id
    ) {
        return ResponseEntity.ok(patientService.getById(id));
    }

    @Operation(summary = "Actualizar información de un paciente")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PatientResponseDTO> update(
            @Parameter(description = "UUID del paciente") @PathVariable UUID id,
            @Valid @RequestBody PatientRequestDTO request
    ) {
        return ResponseEntity.ok(patientService.update(id, request));
    }

    @Operation(summary = "Desactivar un paciente")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
    @Transactional
    public ResponseEntity<?> deactivate(
            @Parameter(description = "UUID del paciente") @PathVariable UUID id
    ) {
        patientService.deactivate(id);
        return ResponseEntity.ok("Paciente desactivado correctamente");
    }

}
