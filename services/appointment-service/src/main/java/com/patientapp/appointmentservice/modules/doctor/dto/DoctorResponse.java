package com.patientapp.appointmentservice.modules.doctor.dto;

import java.util.UUID;

public record DoctorResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String medicalLicense,
        String officeNumber,
        String userId,
        String zoneId,
        Integer appointmentDuration
) {
}