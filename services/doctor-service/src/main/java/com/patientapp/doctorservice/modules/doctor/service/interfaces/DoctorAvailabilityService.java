package com.patientapp.doctorservice.modules.doctor.service.interfaces;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorDayAvailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorMonthAvailabilityResponseDTO;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

public interface DoctorAvailabilityService {
    DoctorMonthAvailabilityResponseDTO getByDoctorIdAndMonth(UUID doctorId, Month month);
    DoctorDayAvailabilityResponseDTO getByDoctorIdAndDay(UUID doctorId, LocalDate date);

}
