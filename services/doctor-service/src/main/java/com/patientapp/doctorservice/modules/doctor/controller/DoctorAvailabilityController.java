package com.patientapp.doctorservice.modules.doctor.controller;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorDayAvailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorMonthAvailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorAvailabilityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@RestController
@RequestMapping("/doctor-availability")
@RequiredArgsConstructor
@Tag(name = "Disponibilidad de Doctor", description = "Disponibilidad real de un doctor combinando horarios y ausencias")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService doctorAvailabilityService;

    @GetMapping("/{doctorId}/month-availability")
    public DoctorMonthAvailabilityResponseDTO getDoctorAvailabilityByMonth(
            @PathVariable UUID doctorId,
            @RequestParam int month
    ) {
        return doctorAvailabilityService.getByDoctorIdAndMonth(doctorId, Month.of(month));
    }


    @GetMapping("/{doctorId}/day-availability")
    public DoctorDayAvailabilityResponseDTO getByDoctorAndDay(
            @PathVariable UUID doctorId,
            @RequestParam String date // 'YYYY-MM-DD'
    ) {
        LocalDate localDate = LocalDate.parse(date);
        return doctorAvailabilityService.getByDoctorIdAndDay(doctorId, localDate);
    }
}
