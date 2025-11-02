package com.patientapp.appointmentservice.modules.dashboard.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AdminDashboardResponse(
        long totalDoctors,
        long totalPatients,
        long totalAppointments,
        long totalCompletedAppointments,
        long totalCancelledAppointments,
        List<Integer> monthlyAppointments,
        List<DoctorSummary> topActiveDoctors,
        List<AppointmentSummary> recentAppointments,
        List<SpecialtyDistribution> specialtiesDistribution
) {
}
