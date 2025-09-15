package com.patientapp.doctorservice.modules.doctor.controller;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorUnavailabilityRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorUnavailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorUnavailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctor-unavailabilities")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_DOCTOR')")
@Tag(name = "Ausencias", description = "Gestión de ausencias puntuales de doctores")
public class DoctorUnavailabilityController {

    private final DoctorUnavailabilityService service;

    @Operation(summary = "Crear ausencia del doctor")
    @PostMapping
    @Transactional
    public ResponseEntity<DoctorUnavailabilityResponseDTO> create(
            @Parameter(description = "Datos de la ausencia a crear")
            @Valid @RequestBody DoctorUnavailabilityRequestDTO request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    @Operation(summary = "Obtener todas las ausencias de un doctor")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorUnavailabilityResponseDTO>> getByDoctorId(
            @Parameter(description = "ID del doctor para filtrar ausencias")
            @PathVariable UUID doctorId
    ) {
        return ResponseEntity.ok(service.getByDoctorId(doctorId));
    }

    @Operation(summary = "Eliminar una ausencia por ID")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la ausencia a eliminar")
            @PathVariable Integer id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
