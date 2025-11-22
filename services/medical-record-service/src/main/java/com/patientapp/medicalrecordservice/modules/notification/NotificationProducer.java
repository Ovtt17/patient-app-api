package com.patientapp.medicalrecordservice.modules.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMedicalRecordCreatedEvent(MedicalRecordCreatedRequest request) {
        log.info("Sending medical record created event to Kafka: {}", request);

        Message<MedicalRecordCreatedRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(TOPIC, "medical-record-created-topic")
                .build();

        kafkaTemplate.send(message);
    }}
