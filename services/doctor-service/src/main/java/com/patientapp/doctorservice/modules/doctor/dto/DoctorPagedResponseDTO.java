package com.patientapp.doctorservice.modules.doctor.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DoctorPagedResponseDTO(
        List<DoctorResponseDTO> doctors,
        int page,
        int totalPages,
        long totalElements
) {
}
