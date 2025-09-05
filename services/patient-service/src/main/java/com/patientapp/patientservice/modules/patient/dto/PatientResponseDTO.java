package com.patientapp.patientservice.modules.patient.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PatientResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
