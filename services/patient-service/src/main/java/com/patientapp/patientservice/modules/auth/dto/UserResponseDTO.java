package com.patientapp.patientservice.modules.auth.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
