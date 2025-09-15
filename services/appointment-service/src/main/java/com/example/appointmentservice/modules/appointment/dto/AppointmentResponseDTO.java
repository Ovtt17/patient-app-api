package com.example.appointmentservice.modules.appointment.dto;

import com.example.appointmentservice.modules.appointment.enums.AppointmentStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record AppointmentResponseDTO(
        Long id,
        UUID doctorId,
        UUID patientId,
        Instant appointmentDate,
        Instant endTime,
        Integer estimatedDurationMinutes,
        String reason,
        String notes,
        AppointmentStatus status,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
