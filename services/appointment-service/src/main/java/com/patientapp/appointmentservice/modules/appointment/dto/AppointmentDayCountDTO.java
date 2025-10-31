package com.patientapp.appointmentservice.modules.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppointmentDayCountDTO {
    private LocalDate date;
    private Long count;
}
