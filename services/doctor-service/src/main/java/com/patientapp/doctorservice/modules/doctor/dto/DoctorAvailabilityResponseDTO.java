package com.patientapp.doctorservice.modules.doctor.dto;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record DoctorAvailabilityResponseDTO(
        UUID doctorId,
        String zoneId,
        List<DayAvailabilityDTO> availability
) {
    @Builder
    public record DayAvailabilityDTO(
            LocalDate date,
            String dayOfWeek,
            List<IntervalDTO> intervals,
            List<IntervalDTO> unavailable
    ) {}

    @Builder
    public record IntervalDTO(
            Instant start,
            Instant end
    ) {}
}