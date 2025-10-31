package com.patientapp.doctorservice.modules.doctor.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DoctorDayAvailabilityResponseDTO(
        UUID doctorId,
        LocalDate date,
        Integer appointmentDurationMinutes,
        List<IntervalDTO> intervals
) {}