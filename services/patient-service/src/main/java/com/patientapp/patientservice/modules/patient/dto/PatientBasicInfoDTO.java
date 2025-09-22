package com.patientapp.patientservice.modules.patient.dto;

import com.patientapp.patientservice.modules.patient.enums.Gender;

public record PatientBasicInfoDTO(
        String firstName,
        String lastName,
        String phone,
        Gender gender
) {
}
