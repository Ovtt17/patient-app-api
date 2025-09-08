package com.patientapp.doctorservice.modules.doctor.mapper;

import com.patientapp.doctorservice.modules.auth.dto.UserResponseDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import com.patientapp.doctorservice.common.utils.NullSafe;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorRequestDTO request, List<Specialty> specialties) {
        Set<Specialty> specialtySet = specialties == null ? new HashSet<>() : new HashSet<>(specialties);
        return Doctor.builder()
                .medicalLicense(NullSafe.ifNotBlankOrNull(request.medicalLicense()))
                .officeNumber(NullSafe.ifNotBlankOrNull(request.officeNumber()))
                .userId(request.userId())
                .specialties(specialtySet)
                .active(true)
                .build();
    }

    public DoctorResponseDTO toDoctorResponse(Doctor doctor) {
        return DoctorResponseDTO.builder()
                .medicalLicense(doctor.getMedicalLicense())
                .officeNumber(doctor.getOfficeNumber())
                .userId(doctor.getUserId().toString())
                .specialties(
                        doctor.getSpecialties() == null ? List.of() :
                                doctor.getSpecialties().stream()
                                        .map(Specialty::getName)
                                        .toList()
                )
                .build();
    }

    public DoctorResponseDTO toDoctorResponse(Doctor doctor, UserResponseDTO user) {
        return DoctorResponseDTO.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .phone(user.phone())
                .medicalLicense(doctor.getMedicalLicense())
                .officeNumber(doctor.getOfficeNumber())
                .userId(doctor.getUserId().toString())
                .specialties(
                        doctor.getSpecialties() == null ? List.of() :
                                doctor.getSpecialties().stream()
                                        .map(Specialty::getName)
                                        .toList()
                )
                .build();
    }
}
