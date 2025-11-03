package com.patientapp.medicalrecordservice.modules.patient.client;

import com.patientapp.medicalrecordservice.config.feign.FeignConfig;
import com.patientapp.medicalrecordservice.modules.patient.dto.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "patient-service",
        url = "${application.config.patient-url}",
        path = "/api/v1/patients",
        configuration = FeignConfig.class
)
public interface PatientClient {
    @GetMapping("/{id}")
    PatientResponse getById(@PathVariable UUID id);
}
