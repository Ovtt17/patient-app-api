package com.patientapp.doctorservice.modules.doctor.dto;

import java.util.UUID;

public record DoctorSummaryDTO(
        UUID id,
        String fullName,
        String specialty,
        long appointmentsCount
) {
}
