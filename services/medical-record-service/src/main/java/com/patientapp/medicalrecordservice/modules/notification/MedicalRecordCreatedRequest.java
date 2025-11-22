package com.patientapp.medicalrecordservice.modules.notification;

import lombok.Builder;

import java.time.Instant;

@Builder
public record MedicalRecordCreatedRequest(
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
