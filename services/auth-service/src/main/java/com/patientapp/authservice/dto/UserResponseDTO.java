package com.patientapp.authservice.dto;

import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        String phone,
        String profilePictureUrl,
        List<String> roles
) {}
