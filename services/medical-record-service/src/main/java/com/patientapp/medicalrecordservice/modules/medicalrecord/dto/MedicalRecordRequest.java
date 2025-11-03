package com.patientapp.medicalrecordservice.modules.medicalrecord.dto;

import java.util.UUID;

public record MedicalRecordRequest(
        UUID patientId,
        UUID doctorId,
        Long appointmentId,
        Double weight,
        Double height,
        String bloodType,
        String allergies,
        String chronicDiseases,
        String medications,
        String diagnostic
) {
}
