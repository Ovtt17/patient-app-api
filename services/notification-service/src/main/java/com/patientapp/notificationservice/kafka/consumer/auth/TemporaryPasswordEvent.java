package com.patientapp.notificationservice.kafka.consumer.auth;

public record TemporaryPasswordEvent(
        String firstName,
        String email,
        String temporaryPassword,
        String loginUrl
) {
}
