package com.patientapp.medicalrecordservice.modules.medicalrecord.dto;

import java.time.Instant;
import java.util.UUID;

public record MedicalRecordResponse(
        Long id,
        UUID patientId,
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
