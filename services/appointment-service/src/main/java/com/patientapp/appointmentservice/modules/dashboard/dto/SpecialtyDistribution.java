package com.patientapp.appointmentservice.modules.dashboard.dto;

import lombok.Builder;

@Builder
public record SpecialtyDistribution(
        String specialty,
        long doctorCount
) {
}
