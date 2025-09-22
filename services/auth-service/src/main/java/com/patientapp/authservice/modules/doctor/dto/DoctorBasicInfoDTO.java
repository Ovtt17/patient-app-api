package com.patientapp.authservice.modules.doctor.dto;

import com.patientapp.authservice.modules.user.enums.Gender;
import lombok.Builder;

@Builder
public record DoctorBasicInfoDTO(
        String firstName,
        String lastName,
        String phone,
        Gender gender
) {
}
