package com.patientapp.doctorservice.modules.doctor.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record DoctorMonthAvailabilityResponseDTO(
        UUID doctorId,
        int year,
        int month,
        List<DayAvailability> availability
) {
    @Builder
    public record DayAvailability(LocalDate date, boolean fullyBooked) {}
}