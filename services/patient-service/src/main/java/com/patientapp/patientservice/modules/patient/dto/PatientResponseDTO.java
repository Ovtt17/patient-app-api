package com.patientapp.patientservice.modules.patient.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record PatientResponseDTO(
        UUID id,
        UUID userId,
        Double weight,
        Double height,
        LocalDate birthDate,
        String notes
) {
}