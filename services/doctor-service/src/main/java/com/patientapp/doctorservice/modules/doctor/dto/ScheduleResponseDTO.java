package com.patientapp.doctorservice.modules.doctor.dto;

import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record ScheduleResponseDTO(
    Integer id,
    UUID doctorId,
    DayOfWeek dayOfWeek,
    LocalTime startTime,
    LocalTime endTime,
    String zoneId
) {
}
