package com.patientapp.doctorservice.modules.doctor.mapper;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorUnavailabilityRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorUnavailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.DoctorUnavailability;
import org.springframework.stereotype.Component;

@Component
public class DoctorUnavailabilityMapper {
    public DoctorUnavailability toEntity(DoctorUnavailabilityRequestDTO dto, Doctor doctor) {
        return DoctorUnavailability.builder()
                .doctor(doctor)
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .available(false)
                .build();
    }

    public DoctorUnavailabilityResponseDTO toResponseDTO(DoctorUnavailability entity) {
        return new DoctorUnavailabilityResponseDTO(
                entity.getId(),
                entity.getDoctor().getId(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }
}
