package com.patientapp.authservice.modules.patient.client;

import com.patientapp.authservice.config.feign.FeignConfig;
import com.patientapp.authservice.modules.patient.dto.PatientRequestDTO;
import com.patientapp.authservice.modules.patient.dto.PatientBasicInfoDTO;
import com.patientapp.authservice.modules.patient.dto.PatientResponse;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{userId}/basic-info")
    ResponseEntity<Void> updateBasicInfo(
            @PathVariable UUID userId,
            @RequestBody PatientBasicInfoDTO request
    );

    @GetMapping("/user/{userId}")
    PatientResponse getByUserId(
            @Parameter(description = "UUID del usuario") @PathVariable UUID userId
    );
}
