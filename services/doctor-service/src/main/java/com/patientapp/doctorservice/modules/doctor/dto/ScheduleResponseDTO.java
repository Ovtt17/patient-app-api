package com.patientapp.doctorservice.modules.doctor.dto;

import lombok.Builder;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.UUID;

@Builder
public record ScheduleResponseDTO(
    Integer id,
    UUID doctorId,
    DayOfWeek dayOfWeek,
    Instant startTime,
    Instant endTime,
    String zoneId
) {
}
