package com.patientapp.appointmentservice.modules.appointment.repository;

import java.util.UUID;

public interface DoctorAppointmentCount {
    UUID getDoctorId();
    Long getCount();
}