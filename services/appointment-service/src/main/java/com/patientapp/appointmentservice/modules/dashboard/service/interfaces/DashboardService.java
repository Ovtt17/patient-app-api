package com.patientapp.appointmentservice.modules.dashboard.service.interfaces;

import com.patientapp.appointmentservice.modules.dashboard.dto.AdminDashboardResponse;
import com.patientapp.appointmentservice.modules.dashboard.dto.DoctorDashboardResponse;

import java.util.UUID;

public interface DashboardService {
    AdminDashboardResponse getAdminDashboard();
    DoctorDashboardResponse getDoctorDashboard(UUID doctorId);
}
