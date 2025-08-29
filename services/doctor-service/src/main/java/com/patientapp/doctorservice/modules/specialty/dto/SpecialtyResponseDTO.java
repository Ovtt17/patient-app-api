package com.patientapp.doctorservice.modules.specialty.dto;

import lombok.Builder;

@Builder
public record SpecialtyResponseDTO(
        Integer id,
        String name,
        String description
) {
}
