package com.patientapp.doctorservice.modules.doctor.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record DoctorUnavailabilityRequestDTO(
        @NotNull(message = "El inicio del periodo es obligatorio")
        Instant startTime,

        @NotNull(message = "El fin del periodo es obligatorio")
        Instant endTime,

        @NotNull(message = "El ID del doctor es obligatorio")
        UUID doctorId
) {
}

