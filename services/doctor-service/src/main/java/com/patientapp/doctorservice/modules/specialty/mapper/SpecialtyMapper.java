package com.patientapp.doctorservice.modules.specialty.mapper;

import com.patientapp.doctorservice.common.utils.NullSafe;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyRequestDTO;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyResponseDTO;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyMapper {
    public Specialty toEntity(SpecialtyRequestDTO request) {
        return Specialty.builder()
                .name(NullSafe.ifNotBlankOrNull(request.name()))
                .description(NullSafe.ifNotBlankOrNull(request.description()))
                .build();
    }

    public SpecialtyResponseDTO toResponseDTO(Specialty specialty) {
        return SpecialtyResponseDTO.builder()
                .id(NullSafe.ifPresentOrNull(specialty.getId()))
                .name(NullSafe.ifNotBlankOrNull(specialty.getName()))
                .description(NullSafe.ifNotBlankOrNull(specialty.getDescription()))
                .build();
    }

    public Specialty update(SpecialtyRequestDTO request, Specialty specialty) {
        specialty.setName(NullSafe.ifNotBlankOrNull(request.name()));
        specialty.setDescription(NullSafe.ifNotBlankOrNull(request.description()));
        return specialty;
    }
}
