package com.patientapp.doctorservice.modules.doctor.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record DoctorResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String medicalLicense,
        String officeNumber,
        String userId,
        String zoneId,
        List<String> specialties
) {
}
