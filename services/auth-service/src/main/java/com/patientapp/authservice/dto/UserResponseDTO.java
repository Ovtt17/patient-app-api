package com.patientapp.authservice.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        String phone,
        String profilePictureUrl,
        List<String> roles
) {}
