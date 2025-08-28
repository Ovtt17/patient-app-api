package com.patientapp.authservice.doctor.dto;

public record DoctorCreatedDTO(
        String email,
        String temporaryPassword
) {
}
