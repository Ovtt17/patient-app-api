package com.patientapp.appointmentservice.modules.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IntervalDTO {
    private Instant start;
    private Instant end;
}