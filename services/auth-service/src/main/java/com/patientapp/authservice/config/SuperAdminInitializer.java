package com.patientapp.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@RequiredArgsConstructor
@Order(2)
public class SuperAdminInitializer implements CommandLineRunner {
    private final SuperAdminService superAdminService;

    @Override
    public void run(String... args) {
        superAdminService.createSuperAdmin();
    }
}
