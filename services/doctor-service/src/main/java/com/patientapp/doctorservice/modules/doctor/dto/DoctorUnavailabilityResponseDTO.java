package com.patientapp.doctorservice.modules.doctor.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record DoctorUnavailabilityResponseDTO(
        Integer id,
        UUID doctorId,
        Instant startTime,
        Instant endTime
) {
}
