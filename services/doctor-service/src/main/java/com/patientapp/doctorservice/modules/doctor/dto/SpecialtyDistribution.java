package com.patientapp.doctorservice.modules.doctor.dto;

import lombok.Builder;

@Builder
public record SpecialtyDistribution(
        String specialty,
        long doctorCount
) {
}
