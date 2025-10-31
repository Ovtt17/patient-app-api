package com.patientapp.appointmentservice.modules.appointment.controller;

import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentFilterDTO;
import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentRequestDTO;
import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentResponseDTO;
import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
import com.patientapp.appointmentservice.modules.appointment.service.interfaces.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Citas", description = "Gestión de citas médicas")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Crear una nueva cita", description = "Permite crear una cita para un paciente con un doctor")
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(
            @Valid @RequestBody AppointmentRequestDTO request
    ) {
        return ResponseEntity.ok(appointmentService.create(request));
    }

    @Operation(summary = "Obtener cita por ID", description = "Permite obtener la información completa de una cita por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getById(
            @Parameter(description = "ID de la cita", required = true, example = "1")
            @PathVariable("id") Long appointmentId
    ) {
        return ResponseEntity.ok(appointmentService.getById(appointmentId));
    }

    @Operation(summary = "Obtener citas de un doctor", description = "Devuelve todas las citas de un doctor a partir de una fecha específica (por defecto, una semana adelante)")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllByDoctor(
            @Parameter(description = "ID del doctor", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID doctorId,

            @Parameter(description = "Fecha inicial (formato ISO-8601). Si no se envía, se usa la fecha actual")
            @RequestParam(required = false) Instant fromDate
    ) {
        return ResponseEntity.ok(appointmentService.getAllByDoctor(doctorId, fromDate));
    }

    @Operation(summary = "Obtener citas de un paciente", description = "Devuelve todas las citas de un paciente a partir de una fecha específica (por defecto, una semana adelante)")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllByPatient(
            @Parameter(description = "ID del paciente", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID patientId,

            @Parameter(description = "Fecha inicial (formato ISO-8601). Si no se envía, se usa la fecha actual")
            @RequestParam(required = false) Instant fromDate
    ) {
        return ResponseEntity.ok(appointmentService.getAllByPatient(patientId, fromDate));
    }

    @Operation(summary = "Filtrar citas", description = "Permite filtrar citas según doctor, paciente, estado o rango de fechas")
    @PostMapping("/filter")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllFiltered(
            @RequestBody AppointmentFilterDTO filter
    ) {
        return ResponseEntity.ok(appointmentService.getAllFiltered(filter));
    }

    @Operation(summary = "Actualizar estado de una cita", description = "Permite actualizar el estado de una cita (PENDIENTE, CONFIRMADA, COMPLETADA, CANCELADA)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @Parameter(description = "ID de la cita", required = true, example = "1")
            @PathVariable("id") Long appointmentId,

            @Parameter(description = "Nuevo estado de la cita", required = true, example = "CONFIRMADA")
            @RequestParam AppointmentStatus status
    ) {
        appointmentService.updateStatus(appointmentId, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancelar una cita", description = "Permite cancelar una cita y registra quién la canceló")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @Parameter(description = "ID de la cita a cancelar", required = true, example = "1")
            @PathVariable("id") Long appointmentId
    ) {
        appointmentService.cancel(appointmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/{doctorId}/month-summary")
    public Map<LocalDate, Long> getAppointmentCountByDoctorAndMonth(
            @PathVariable UUID doctorId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return appointmentService.getAppointmentCountByDoctorAndMonth(doctorId, year, month);
    }

    @GetMapping("/doctor/{doctorId}/day-appointments")
    List<Instant> getAppointmentsByDoctorAndDay(
            @PathVariable("doctorId") UUID doctorId,
            @RequestParam LocalDate date
    ) {
        return appointmentService.getAppointmentsByDoctorAndDay(doctorId, date);
    }

}
