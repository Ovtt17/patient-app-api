package com.patientapp.authservice.modules.user.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UserResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String username,
        String profilePictureUrl,
        List<String> roles,
        boolean mustChangePassword
) {}
