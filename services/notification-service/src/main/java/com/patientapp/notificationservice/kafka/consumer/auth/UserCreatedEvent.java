package com.patientapp.notificationservice.kafka.consumer.auth;

public record UserCreatedEvent(
        String userId,
        String firstName,
        String email,
        String activationCode,
        String confirmationUrl
) {}