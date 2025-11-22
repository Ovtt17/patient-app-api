package com.patientapp.notificationservice.kafka.consumer.medicalrecord;

import java.time.Instant;

public record MedicalRecordCreatedEvent(
        Long id,
        String patientName,
        String patientEmail,
        String doctorName,
        String doctorEmail,
        Long appointmentId,
        Double weight,
        Double height,
        String bloodType,
        String allergies,
        String chronicDiseases,
        String medications,
        String diagnostic,
        Instant createdDate
) {
}
