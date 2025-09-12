package com.patientapp.doctorservice.modules.doctor.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.UUID;

public record ScheduleRequestDTO(
        @NotNull(message = "El día de la semana es obligatorio")
        DayOfWeek dayOfWeek,

        @NotNull(message = "La hora de inicio es obligatoria")
        Instant startTime,

        @NotNull(message = "La hora de fin es obligatoria")
        Instant endTime,

        @NotNull(message = "El ID del doctor es obligatorio")
        UUID doctorId
) {
}

