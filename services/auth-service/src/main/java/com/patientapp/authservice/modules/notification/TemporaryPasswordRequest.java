package com.patientapp.authservice.modules.notification;

import lombok.Builder;

@Builder
public record TemporaryPasswordRequest(
        String firstName,
        String email,
        String temporaryPassword,
        String loginUrl
) {
}
