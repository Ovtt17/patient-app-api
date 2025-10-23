package com.patientapp.doctorservice.modules.doctor.service.interfaces;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorAvailabilityResponseDTO;

import java.util.UUID;

public interface DoctorAvailabilityService {
    /**
     * Gets the actual availability of a doctor, combining their working hours and absences.
     *
     * @param doctorId UUID of the doctor
     * @return DoctorAvailabilityResponseDTO with available intervals and absences
     */
    DoctorAvailabilityResponseDTO getByDoctorId(UUID doctorId);
    /**
     * Gets the actual availability of the authenticated doctor, combining their working hours and absences.
     *
     * @return DoctorAvailabilityResponseDTO with available intervals and absences
     */
    DoctorAvailabilityResponseDTO getMyAvailability();
}
