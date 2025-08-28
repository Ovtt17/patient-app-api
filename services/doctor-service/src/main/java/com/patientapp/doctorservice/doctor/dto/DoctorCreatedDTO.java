package com.patientapp.doctorservice.doctor.dto;

public record DoctorCreatedDTO(
        String email,
        String temporaryPassword
) {
}
