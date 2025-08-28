package com.patientapp.doctorservice.doctor.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DoctorResponseDTO(
        String firstName,
        String lastName,
        String email,
        String phone,
        String medicalLicense,
        String officeNumber,
        String userId,
        List<String> specialties
) {
}
