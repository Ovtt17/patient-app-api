package com.patientapp.authservice.modules.patient.dto;

import com.patientapp.authservice.modules.user.enums.Gender;
import lombok.Builder;

@Builder
public record PatientBasicInfoDTO(
        String firstName,
        String lastName,
        String phone,
        Gender gender
) {
}
