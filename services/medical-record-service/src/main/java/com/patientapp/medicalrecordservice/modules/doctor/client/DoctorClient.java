package com.patientapp.medicalrecordservice.modules.doctor.client;

import com.patientapp.medicalrecordservice.config.feign.FeignConfig;
import com.patientapp.medicalrecordservice.modules.doctor.dto.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
