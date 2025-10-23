package com.patientapp.doctorservice.modules.doctor.mapper;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.specialty.mapper.SpecialtyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoctorMapper {

    private final SpecialtyMapper specialtyMapper;

    public DoctorMapper(SpecialtyMapper specialtyMapper) {
        this.specialtyMapper = specialtyMapper;
    }

    public Doctor toEntity(DoctorRequestDTO request) {
        return Doctor.builder()
                .userId(request.userId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .gender(request.gender())
                .profilePicture(request.profilePictureUrl())
                .active(true)
                .build();
    }

    public DoctorResponseDTO toDoctorResponse(Doctor doctor) {
        return DoctorResponseDTO.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .gender(doctor.getGender())
                .profilePictureUrl(doctor.getProfilePicture())
                .medicalLicense(doctor.getMedicalLicense())
                .officeNumber(doctor.getOfficeNumber())
                .userId(doctor.getUserId().toString())
                .zoneId(doctor.getZoneId())
                .appointmentDuration(doctor.getAppointmentDuration())
                .specialties(
                        doctor.getSpecialties() != null ?
                                doctor.getSpecialties().stream()
                                        .map(specialtyMapper::toResponseDTO)
                                        .toList()
                                : List.of()
                )
                .build();
    }
}
