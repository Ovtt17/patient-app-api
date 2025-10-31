package com.patientapp.authservice.modules.user.dto;

import com.patientapp.authservice.modules.user.enums.Gender;
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
        Gender gender,
        String bio,
        boolean mustChangePassword,
        UUID patientId,
        UUID doctorId
) {}
