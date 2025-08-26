package com.patientapp.doctorservice.doctor.mapper;

import com.patientapp.doctorservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.doctor.entity.Doctor;
import com.patientapp.doctorservice.specialty.entity.Specialty;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorRequestDTO dto, List<Specialty> specialties) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());
        doctor.setPhone(dto.getPhone());
        doctor.setMedicalLicense(dto.getMedicalLicense());
        doctor.setOfficeNumber(dto.getOfficeNumber());
        doctor.setUserId(dto.getUserId());
        doctor.setSpecialties(new HashSet<>(specialties));
        doctor.setActive(true);
        return doctor;
    }
}
