package com.patientapp.appointmentservice.modules.notification;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AppointmentCreatedRequest(
        String appointmentId,
        String patientName,
        String patientEmail,
        String doctorName,
        String doctorEmail,
        String doctorZoneId,
        Instant appointmentDate
) {
}
