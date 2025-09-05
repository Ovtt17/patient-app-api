package com.patientapp.patientservice.modules.patient.mapper;

import com.patientapp.patientservice.modules.patient.dto.PatientPagedResponseDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientRequestDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientResponseDTO;
import com.patientapp.patientservice.modules.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequestDTO request) {
        return Patient.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .userId(request.userId())
                .build();
    }

    public PatientResponseDTO toPatientResponse(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .build();
    }

    public PatientPagedResponseDTO toPatientPagedResponseDTO(Page<Patient> patients) {
        return PatientPagedResponseDTO.builder()
                .patients(patients
                        .map(this::toPatientResponse)
                        .getContent()
                )
                .page(patients.getNumber())
                .totalPages(patients.getTotalPages())
                .totalElements(patients.getTotalElements())
                .build();
    }
}
