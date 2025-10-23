package com.patientapp.doctorservice.modules.doctor.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ScheduleRequestDTO(
        @NotNull(message = "El día de la semana es obligatorio")
        DayOfWeek dayOfWeek,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime startTime,

        @NotNull(message = "La hora de fin es obligatoria")
        LocalTime endTime
) {
}

