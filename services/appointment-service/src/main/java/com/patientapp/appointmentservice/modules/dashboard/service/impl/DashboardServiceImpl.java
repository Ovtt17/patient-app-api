package com.patientapp.appointmentservice.modules.dashboard.service.impl;

import com.patientapp.appointmentservice.modules.appointment.service.interfaces.AppointmentService;
import com.patientapp.appointmentservice.modules.dashboard.dto.*;
import com.patientapp.appointmentservice.modules.dashboard.service.interfaces.DashboardService;
import com.patientapp.appointmentservice.modules.doctor.client.DoctorClient;
import com.patientapp.appointmentservice.modules.patient.client.PatientClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DoctorClient doctorClient;
    private final PatientClient patientClient;
    private final AppointmentService service;

    @Override
    public AdminDashboardResponse getAdminDashboard() {
        Long totalDoctors = doctorClient.countAll();
        Long totalPatients = patientClient.countAll();
        Long totalAppointments = service.countAllByCurrentMonth();
        Long totalCompleted = service.countAllByCurrentMonthAndCompleted();
        Long totalCancelled = service.countAllByCurrentMonthAndCancelled();

        List<Integer> monthlyAppointments = service.getMonthlyAppointments();
        List<AppointmentSummary> recentAppointments = service.findRecentAppointments();
        List<DoctorSummary> topDoctors = service.findTopActiveDoctors();
        List<SpecialtyDistribution> specialties = doctorClient.countBySpecialty();

        return AdminDashboardResponse.builder()
                .totalDoctors(totalDoctors)
                .totalPatients(totalPatients)
                .totalAppointments(totalAppointments)
                .totalCompletedAppointments(totalCompleted)
                .totalCancelledAppointments(totalCancelled)
                .monthlyAppointments(monthlyAppointments)
                .recentAppointments(recentAppointments)
                .topActiveDoctors(topDoctors)
                .specialtiesDistribution(specialties)
                .build();
    }
}
