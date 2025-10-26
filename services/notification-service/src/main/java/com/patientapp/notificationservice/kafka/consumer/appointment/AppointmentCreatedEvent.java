package com.patientapp.notificationservice.kafka.consumer.appointment;

import java.time.Instant;

public record AppointmentCreatedEvent(
        String appointmentId,
        String patientName,
        String patientEmail,
        String doctorName,
        String doctorEmail,
        String doctorZoneId,
        Instant appointmentStart
) {
}
