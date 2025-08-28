package com.patientapp.authservice.doctor.client;

import com.patientapp.authservice.config.feign.FeignConfig;
import com.patientapp.authservice.doctor.dto.DoctorRequestDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "doctor-service",
        url = "${application.config.doctor-url}",
        configuration = FeignConfig.class
)
public interface DoctorClient {
    @PostMapping
    UUID create(@Valid @RequestBody DoctorRequestDTO request);
}
