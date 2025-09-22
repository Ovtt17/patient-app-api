package com.patientapp.doctorservice.modules.doctor.dto;

import com.patientapp.doctorservice.modules.doctor.enums.Gender;

public record DoctorBasicInfoDTO(
        String firstName,
        String lastName,
        String phone,
        Gender gender
) {
}
