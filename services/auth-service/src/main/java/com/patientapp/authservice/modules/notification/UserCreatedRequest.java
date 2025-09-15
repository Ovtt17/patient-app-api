package com.patientapp.authservice.modules.notification;

import lombok.Builder;

@Builder
public record UserCreatedRequest(
        String userId,
        String firstName,
        String email,
        String activationCode,
        String confirmationUrl
) {}