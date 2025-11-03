package com.patientapp.notificationservice.kafka.consumer.medicalrecord;

import com.patientapp.notificationservice.notification.handler.MedicalRecordNotificationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordEventConsumer {
    private final MedicalRecordNotificationHandler medicalRecordNotificationHandler;

    @KafkaListener(topics = "medical-record-created-topic", groupId = "medicalRecordGroup")
    public void consumeMedicalRecordCreatedEvent(MedicalRecordCreatedEvent event) {
        log.info("Received medical record creation event for record ID: {}", event.id());
        medicalRecordNotificationHandler.handleMedicalRecordCreated(event);
    }
}
