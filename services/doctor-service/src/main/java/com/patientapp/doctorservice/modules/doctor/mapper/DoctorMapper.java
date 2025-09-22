package com.patientapp.doctorservice.modules.doctor.mapper;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoctorMapper {

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
                .specialties(
                        doctor.getSpecialties() == null ? List.of() :
                                doctor.getSpecialties().stream()
                                        .map(Specialty::getName)
                                        .toList()
                )
                .build();
    }
}
