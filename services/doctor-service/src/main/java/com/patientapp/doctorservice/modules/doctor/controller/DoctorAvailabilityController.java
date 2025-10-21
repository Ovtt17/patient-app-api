package com.patientapp.doctorservice.modules.doctor.controller;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorAvailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorAvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/doctor-availability")
@RequiredArgsConstructor
@Tag(name = "Disponibilidad de Doctor", description = "Disponibilidad real de un doctor combinando horarios y ausencias")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService doctorAvailabilityService;

    @Operation(summary = "Obtener disponibilidad real de un doctor")
    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorAvailabilityResponseDTO> getByDoctorId(
            @Parameter(description = "UUID del doctor")
            @PathVariable UUID doctorId
    ) {
        DoctorAvailabilityResponseDTO availability = doctorAvailabilityService.getByDoctorId(doctorId);
        return ResponseEntity.ok(availability);
    }

    @Operation(summary = "Obtener disponibilidad del doctor con sesión iniciada")
    @GetMapping("/me")
    public ResponseEntity<DoctorAvailabilityResponseDTO> getMyAvailability() {
        return ResponseEntity.ok(doctorAvailabilityService.getMyAvailability());
    }
}
