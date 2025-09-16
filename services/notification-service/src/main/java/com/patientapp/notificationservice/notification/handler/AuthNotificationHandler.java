package com.patientapp.notificationservice.notification.handler;

import com.patientapp.notificationservice.channel.email.AuthEmailService;
import com.patientapp.notificationservice.kafka.consumer.auth.TemporaryPasswordEvent;
import com.patientapp.notificationservice.kafka.consumer.auth.UserCreatedEvent;
import com.patientapp.notificationservice.notification.entity.Notification;
import com.patientapp.notificationservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static com.patientapp.notificationservice.notification.enums.NotificationType.EMAIL_CONFIRMATION;
import static com.patientapp.notificationservice.notification.enums.NotificationType.TEMP_PASSWORD;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthNotificationHandler {
    private final NotificationService notificationService;
    private final AuthEmailService authEmailService;

    public void handleUserCreated(UserCreatedEvent event) {
        Notification notification = Notification.builder()
                .type(EMAIL_CONFIRMATION)
                .notificationDate(Instant.now())
                .sent(false)
                .userCreatedEvent(event)
                .build();

        notificationService.save(notification);
        CompletableFuture<Boolean> sentFuture = authEmailService.sendAccountActivationEmail(event);

        sentFuture.thenAccept(sent -> {
            if (sent) {
                notification.setSent(true);
                notificationService.save(notification); // update in Mongo
                log.info("user activation notification sent successfully to {}", event.email());
            } else {
                log.warn("Failed to send user activation notification to {}", event.email());
            }
        });
    }

    public void handleTemporaryPassword(TemporaryPasswordEvent event) {
        Notification notification = Notification.builder()
                .type(TEMP_PASSWORD)
                .notificationDate(Instant.now())
                .sent(false)
                .temporaryPasswordEvent(event)
                .build();

        notificationService.save(notification);
        CompletableFuture<Boolean> sentFuture = authEmailService.sendTemporaryPasswordEmail(event);

        sentFuture.thenAccept(sent -> {
            if (sent) {
                notification.setSent(true);
                notificationService.save(notification);
                log.info("temporary password notification sent successfully to {}", event.email());
            } else {
                log.warn("Failed to send temporary password notification to {}", event.email());
            }
        });
    }
}
