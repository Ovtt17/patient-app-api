package com.patientapp.doctorservice.modules.specialty.controller;

import com.patientapp.doctorservice.modules.doctor.dto.SpecialtyDistribution;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyRequestDTO;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyResponseDTO;
import com.patientapp.doctorservice.modules.specialty.service.SpecialtyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('doctor:read')")
@Tag(name = "Especialidades", description = "Gestión de especialidades médicas")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @Operation(summary = "Crear una nueva especialidad", description = "Crea una especialidad médica. Requiere rol ADMIN. Devuelve la especialidad creada con su ID.")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<SpecialtyResponseDTO> create(
            @Valid @RequestBody SpecialtyRequestDTO request
    ) {
        var response = specialtyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener todas las especialidades", description = "Obtiene la lista completa de especialidades disponibles.")
    @GetMapping
    public ResponseEntity<List<SpecialtyResponseDTO>> getAll() {
        return ResponseEntity.ok(specialtyService.getAll());
    }

    @Operation(summary = "Obtener una especialidad por ID", description = "Obtiene una especialidad existente por su ID. Devuelve 404 si no existe.")
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> getById(
            @Parameter(description = "ID de la especialidad") @PathVariable Integer id
    ) {
        return ResponseEntity.ok(specialtyService.getById(id));
    }

    @Operation(summary = "Actualizar una especialidad", description = "Actualiza los datos de una especialidad existente. Requiere rol ADMIN.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<SpecialtyResponseDTO> update(
            @Parameter(description = "ID de la especialidad") @PathVariable Integer id,
            @Valid @RequestBody SpecialtyRequestDTO request
    ) {
        return ResponseEntity.ok(specialtyService.update(id, request));
    }

    @Operation(summary = "Eliminar una especialidad", description = "Elimina una especialidad por su ID. Requiere rol ADMIN. Retorna un mensaje de confirmación.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<String> delete(
            @Parameter(description = "ID de la especialidad") @PathVariable Integer id
    ) {
        String message = specialtyService.delete(id);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/count-by-specialty")
    @Operation(summary = "Contar doctores por especialidad", description = "Obtiene el conteo de doctores agrupados por especialidad.")
    public ResponseEntity<List<SpecialtyDistribution>> countBySpecialty() {
        return ResponseEntity.ok(specialtyService.countBySpecialty());
    }
}
