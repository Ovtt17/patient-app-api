package com.patientapp.authservice.modules.patient.client;

import com.patientapp.authservice.config.feign.FeignConfig;
import com.patientapp.authservice.modules.patient.dto.PatientRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "patient-service",
        url = "${application.config.patient-url}",
        path = "/api/v1/patients",
        configuration = FeignConfig.class
)
public interface PatientClient {
    @PostMapping
    UUID create(@RequestBody PatientRequestDTO request);
}
