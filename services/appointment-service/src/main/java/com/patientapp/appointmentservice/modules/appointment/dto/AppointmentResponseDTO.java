package com.patientapp.appointmentservice.modules.appointment.dto;

import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record AppointmentResponseDTO(
        Long id,
        UUID doctorId,
        String doctorName,
        UUID patientId,
        String patientName,
        Instant appointmentStart,
        Instant appointmentEnd,
        Integer plannedDurationMinutes,
        String reason,
        String notes,
        AppointmentStatus status,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
