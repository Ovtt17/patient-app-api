package com.patientapp.appointmentservice.modules.dashboard.controller;

import com.patientapp.appointmentservice.modules.dashboard.dto.AdminDashboardResponse;
import com.patientapp.appointmentservice.modules.dashboard.dto.DoctorDashboardResponse;
import com.patientapp.appointmentservice.modules.dashboard.service.interfaces.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard Médico", description = "Gestión de los dashboards médicos")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Obtener el dashboard administrativo", description = "Obtiene las métricas y estadísticas clave para el dashboard administrativo.")
    public ResponseEntity<AdminDashboardResponse> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @Operation(summary = "Obtener el dashboard del médico", description = "Obtiene las métricas y estadísticas clave para el dashboard del médico.")
    public ResponseEntity<DoctorDashboardResponse> getDoctorDashboard(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(dashboardService.getDoctorDashboard(doctorId));
    }
}
