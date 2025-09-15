package com.patientapp.notificationservice.kafka.consumer.auth;

import com.patientapp.notificationservice.notification.handler.AuthNotificationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthEventConsumer {
    private final AuthNotificationHandler authNotificationHandler;

    @KafkaListener(topics = "user-created-topic", groupId = "authGroup")
    public void consumeUserCreatedEvent(UserCreatedEvent event) {
        log.info("Received user creation event for user: {}", event.email());
        authNotificationHandler.handleUserCreated(event);
    }
}
