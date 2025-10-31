package com.patientapp.appointmentservice.modules.doctor.client;

import com.patientapp.appointmentservice.config.feign.FeignConfig;
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
        path = "/api/v1/doctors",
        configuration = FeignConfig.class
)
public interface DoctorClient {
    @GetMapping("/{id}")
    DoctorResponse getById(@PathVariable UUID id);

    @PostMapping("/by-ids")
    List<DoctorResponse> getByIds(@RequestBody List<UUID> ids);
}
