package com.patientapp.appointmentservice.modules.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record AppointmentRequestDTO(
        @NotNull(message = "El ID del doctor es obligatorio")
        UUID doctorId,

        @NotNull(message = "El ID de usuario del paciente es obligatorio")
        UUID userId,

        UUID patientId,

        @NotNull(message = "La fecha y hora de la cita es obligatoria")
        @Future(message = "La fecha de la cita debe ser en el futuro")
        Instant appointmentStart,

        @NotBlank(message = "El motivo de la cita es obligatorio")
        String reason
) {
}
