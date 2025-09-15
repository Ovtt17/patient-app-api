package com.example.appointmentservice.modules.appointment.dto;

import com.example.appointmentservice.modules.appointment.enums.AppointmentStatus;

import java.time.Instant;
import java.util.UUID;

public record AppointmentFilterDTO(
        UUID doctorId,
        UUID patientId,
        AppointmentStatus status,
        Instant startDate,
        Instant endDate
) {
}
