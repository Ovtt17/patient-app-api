package com.patientapp.doctorservice.modules.doctor.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DoctorPagedResponseDTO(
        List<DoctorResponseDTO> content,
        int page,
        int totalPages,
        long totalElements
) {
}
