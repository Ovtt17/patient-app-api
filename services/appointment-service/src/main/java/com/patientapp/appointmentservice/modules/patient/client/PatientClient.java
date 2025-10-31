package com.patientapp.appointmentservice.modules.patient.client;

import com.patientapp.appointmentservice.config.feign.FeignConfig;
import com.patientapp.appointmentservice.modules.patient.dto.PatientResponse;
import io.swagger.v3.oas.annotations.Parameter;
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

    @GetMapping("/user/{userId}")
    PatientResponse getByUserId(
            @Parameter(description = "UUID del usuario") @PathVariable UUID userId
    );
}
