package com.patientapp.appointmentservice.modules.doctor.client;

import com.patientapp.appointmentservice.config.feign.FeignConfig;
import com.patientapp.appointmentservice.modules.dashboard.dto.DoctorSummary;
import com.patientapp.appointmentservice.modules.dashboard.dto.SpecialtyDistribution;
import com.patientapp.appointmentservice.modules.doctor.dto.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "doctor-service",
        url = "${application.config.doctor-url}",
        path = "/api/v1",
        configuration = FeignConfig.class
)
public interface DoctorClient {
    @GetMapping("/doctors/{id}")
    DoctorResponse getById(@PathVariable UUID id);

    @PostMapping("/doctors/by-ids")
    List<DoctorResponse> getByIds(@RequestBody List<UUID> ids);

    @GetMapping("/doctors/count-all")
    Long countAll();

    @GetMapping("/doctors/top-active")
    List<DoctorSummary> getTopActive();

    @GetMapping("/specialties/count-by-specialty")
    List<SpecialtyDistribution> countBySpecialty();
}
