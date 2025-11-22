package com.patientapp.notificationservice.notification.handler;

import com.patientapp.notificationservice.channel.email.MedicalRecordEmailService;
import com.patientapp.notificationservice.kafka.consumer.medicalrecord.MedicalRecordCreatedEvent;
import com.patientapp.notificationservice.notification.entity.Notification;
import com.patientapp.notificationservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static com.patientapp.notificationservice.notification.enums.NotificationType.MEDICAL_RECORD_CREATED;

@Component
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordNotificationHandler {
    private final NotificationService notificationService;
    private final MedicalRecordEmailService medicalRecordEmailService;

    public void handleMedicalRecordCreated(MedicalRecordCreatedEvent event) {
        Notification notification = Notification.builder()
                .type(MEDICAL_RECORD_CREATED)
                .notificationDate(Instant.now())
                .sent(false)
                .medicalRecordCreatedEvent(event)
                .build();

        notificationService.save(notification);
        CompletableFuture<Boolean> sentFuture = medicalRecordEmailService.sendMedicalRecordCreatedEmail(event);

        sentFuture.thenAccept(sent -> {
            if (sent) {
                notification.setSent(true);
                notificationService.save(notification); // update in Mongo
                log.info("Medical record created notification sent successfully to {}", event.patientEmail());
            } else {
                log.warn("Failed to send medical record created notification to {}", event.patientEmail());
            }
        });
    }
}
