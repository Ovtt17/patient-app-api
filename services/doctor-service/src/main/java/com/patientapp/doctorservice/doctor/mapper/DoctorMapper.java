package com.patientapp.doctorservice.doctor.mapper;

import com.patientapp.doctorservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.doctor.entity.Doctor;
import com.patientapp.doctorservice.specialty.entity.Specialty;
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
        doctor.setMedicalLicense(request.medicalLicense());
        doctor.setOfficeNumber(request.officeNumber());
        doctor.setUserId(request.userId());
        doctor.setSpecialties(new HashSet<>(specialties));
        doctor.setActive(true);
        return doctor;
    }
}
