package com.patientapp.patientservice.modules.patient.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PatientPagedResponseDTO(
        List<PatientResponseDTO> content,
        int page,
        int totalPages,
        long totalElements
) {
}
