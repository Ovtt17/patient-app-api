package com.patientapp.appointmentservice.modules.dashboard.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DoctorSummary(
        UUID id,
        String fullName,
        long appointmentsCount
) {
}
