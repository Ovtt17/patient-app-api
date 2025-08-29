package com.patientapp.doctorservice.modules.specialty.controller;

import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyRequestDTO;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyResponseDTO;
import com.patientapp.doctorservice.modules.specialty.service.SpecialtyService;
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

import java.util.List;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Especialidades", description = "Gestión de especialidades médicas")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @Operation(summary = "Crear una nueva especialidad")
    @PostMapping
    @Transactional
    public ResponseEntity<SpecialtyResponseDTO> create(
            @Valid @RequestBody SpecialtyRequestDTO request
    ) {
        var response = specialtyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener todas las especialidades")
    @GetMapping
    public ResponseEntity<List<SpecialtyResponseDTO>> getAll() {
        return ResponseEntity.ok(specialtyService.getAll());
    }

    @Operation(summary = "Obtener una especialidad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> getById(
            @Parameter(description = "ID de la especialidad") @PathVariable Integer id
    ) {
        return ResponseEntity.ok(specialtyService.getById(id));
    }

    @Operation(summary = "Actualizar una especialidad")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<SpecialtyResponseDTO> update(
            @Parameter(description = "ID de la especialidad") @PathVariable Integer id,
            @Valid @RequestBody SpecialtyRequestDTO request
    ) {
        return ResponseEntity.ok(specialtyService.update(id, request));
    }

    @Operation(summary = "Eliminar una especialidad")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> delete(
            @Parameter(description = "ID de la especialidad") @PathVariable Integer id
    ) {
        String message = specialtyService.delete(id);
        return ResponseEntity.ok(message);
    }
}
