package com.patientapp.doctorservice.modules.appointment.client;

import com.patientapp.doctorservice.config.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(
        name = "appointment-service",
        url = "${application.config.appointment-url}",
        path = "/api/v1/appointments",
        configuration = FeignConfig.class
)
public interface AppointmentClient {

    @GetMapping("/doctor/{doctorId}/month-summary")
    Map<LocalDate, Long> getAppointmentCountByDoctorAndMonth(
            @PathVariable("doctorId")UUID doctorId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    );

    @GetMapping("/doctor/{doctorId}/day-appointments")
    List<Instant> getAppointmentsByDoctorAndDay(
            @PathVariable("doctorId") UUID doctorId,
            @RequestParam LocalDate date
    );
}
