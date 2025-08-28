package com.patientapp.authservice.modules.doctor.dto;

public record DoctorCreatedDTO(
        String email,
        String temporaryPassword
) {
}
