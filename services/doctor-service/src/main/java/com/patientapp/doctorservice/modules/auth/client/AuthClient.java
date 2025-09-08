package com.patientapp.doctorservice.modules.auth.client;

import com.patientapp.doctorservice.config.feign.FeignConfig;
import com.patientapp.doctorservice.modules.auth.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "auth-service",
        url = "${application.config.auth-url}",
        path = "/api/v1",
        configuration = FeignConfig.class
)
public interface AuthClient {
    @GetMapping("/users/{userId}")
    UserResponseDTO getUserById(@PathVariable UUID userId);
}
