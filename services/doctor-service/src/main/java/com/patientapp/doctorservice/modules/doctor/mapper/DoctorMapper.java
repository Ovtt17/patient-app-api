package com.patientapp.doctorservice.modules.doctor.mapper;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import com.patientapp.doctorservice.common.utils.NullSafe;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorRequestDTO request, List<Specialty> specialties) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setMedicalLicense(NullSafe.ifNotBlankOrNull(request.medicalLicense()));
        doctor.setOfficeNumber(NullSafe.ifNotBlankOrNull(request.officeNumber()));
        doctor.setUserId(request.userId());
        doctor.setSpecialties(new HashSet<>(specialties));
        return doctor;
    }

    public DoctorResponseDTO toDoctorResponse(Doctor doctor) {
        return DoctorResponseDTO.builder()
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
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
