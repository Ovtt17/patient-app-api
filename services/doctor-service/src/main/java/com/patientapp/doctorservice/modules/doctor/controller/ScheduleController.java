package com.patientapp.doctorservice.modules.doctor.controller;

import com.patientapp.doctorservice.modules.doctor.dto.ScheduleRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.ScheduleResponseDTO;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
@Tag(name = "Horarios", description = "Gestión de horarios de doctores")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "Crear un nuevo horario")
    @PostMapping
    @Transactional
    public ResponseEntity<ScheduleResponseDTO> create(
            @Valid @RequestBody ScheduleRequestDTO request
    ) {
        return ResponseEntity.ok(scheduleService.create(request));
    }

    @Operation(summary = "Obtener todos los horarios de un doctor (opcional filtrar por día)")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ScheduleResponseDTO>> getByDoctorId(
            @Parameter(description = "UUID del doctor") @PathVariable UUID doctorId,
            @Parameter(description = "Día de la semana opcional para filtrar")
            @RequestParam(required = false) DayOfWeek dayOfWeek
    ) {
        List<ScheduleResponseDTO> schedules = (dayOfWeek != null)
                ? scheduleService.getByDoctorIdAndDay(doctorId, dayOfWeek)
                : scheduleService.getByDoctorId(doctorId);

        return ResponseEntity.ok(schedules);
    }

    @Operation(summary = "Obtener un horario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> getById(
            @Parameter(description = "ID del horario") @PathVariable Integer id
    ) {
        return ResponseEntity.ok(scheduleService.getById(id));
    }

    @Operation(summary = "Actualizar un horario por ID")
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> update(
            @Parameter(description = "ID del horario") @PathVariable Integer id,
            @Valid @RequestBody ScheduleRequestDTO request
    ) {
        return ResponseEntity.ok(scheduleService.update(id, request));
    }

    @Operation(summary = "Eliminar un horario por ID")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del horario") @PathVariable Integer id
    ) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar todos los horarios de un doctor por su UUID")
    @DeleteMapping("/doctor/{doctorId}")
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllByDoctorId(
            @Parameter(description = "UUID del doctor") @PathVariable UUID doctorId
    ) {
        scheduleService.deleteAllByDoctorId(doctorId);
        return ResponseEntity.noContent().build();
    }
}
