package com.patientapp.appointmentservice.modules.patient.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        UUID userId,
        Double weight,
        Double height,
        LocalDate birthDate,
        String notes
) {
}
