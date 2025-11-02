package com.patientapp.appointmentservice.modules.dashboard.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DoctorDashboardResponse(
        long totalPatients,
        long totalAppointments,
        long totalCompletedAppointments,
        long totalCancelledAppointments,
        List<Integer> monthlyAppointments,
        List<AppointmentSummary> recentAppointments
) {
}
