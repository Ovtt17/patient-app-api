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
                .userId(request.userId())
                .weight(request.weight())
                .height(request.height())
                .birthDate(request.birthDate())
                .notes(request.notes().trim())
                .build();
    }

    public PatientResponseDTO toPatientResponse(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .userId(patient.getUserId())
                .weight(patient.getWeight())
                .height(patient.getHeight())
                .birthDate(patient.getBirthDate())
                .notes(patient.getNotes())
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
