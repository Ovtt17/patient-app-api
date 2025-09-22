package com.patientapp.authservice.modules.doctor.client;

import com.patientapp.authservice.config.feign.FeignConfig;
import com.patientapp.authservice.modules.doctor.dto.DoctorRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "doctor-service",
        url = "${application.config.doctor-url}",
        path = "/api/v1/doctors",
        configuration = FeignConfig.class
)
public interface DoctorClient {
    @PostMapping
    UUID create(@RequestBody DoctorRequestDTO request);
}
