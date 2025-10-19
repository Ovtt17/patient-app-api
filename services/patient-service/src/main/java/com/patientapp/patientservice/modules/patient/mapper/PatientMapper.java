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
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .gender(request.gender())
                .profilePicture(request.profilePictureUrl())
                .active(true)
                .build();
    }

    public PatientResponseDTO toPatientResponse(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .gender(patient.getGender())
                .profilePictureUrl(patient.getProfilePicture())
                .weight(patient.getWeight())
                .height(patient.getHeight())
                .birthDate(patient.getBirthDate())
                .notes(patient.getNotes())
                .userId(patient.getUserId())
                .build();
    }

    public PatientPagedResponseDTO toPatientPagedResponseDTO(Page<Patient> patients) {
        return PatientPagedResponseDTO.builder()
                .content(patients
                        .map(this::toPatientResponse)
                        .getContent()
                )
                .page(patients.getNumber())
                .totalPages(patients.getTotalPages())
                .totalElements(patients.getTotalElements())
                .build();
    }
}
