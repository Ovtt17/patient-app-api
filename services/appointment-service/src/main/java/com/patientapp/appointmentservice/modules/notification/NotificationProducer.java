package com.patientapp.appointmentservice.modules.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAppointmentCreatedEvent(AppointmentCreatedRequest request) {
        log.info("Sending appointment created event to Kafka: {}", request);

        Message<AppointmentCreatedRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(TOPIC, "appointment-created-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
