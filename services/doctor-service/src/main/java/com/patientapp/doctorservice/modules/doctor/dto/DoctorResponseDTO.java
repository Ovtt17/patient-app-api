package com.patientapp.doctorservice.modules.doctor.dto;

import com.patientapp.doctorservice.modules.doctor.enums.Gender;
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
        Gender gender,
        String profilePictureUrl,
        String medicalLicense,
        String officeNumber,
        String userId,
        String zoneId,
        List<String> specialties
) {
}