package com.patientapp.appointmentservice.modules.dashboard.dto;

import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
import lombok.Builder;

@Builder
public record AppointmentSummary(
        Long id,
        String patientName,
        String doctorName,
        String date,
        AppointmentStatus status
) {
}
