package com.patientapp.authservice.modules.notification;

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

    private final KafkaTemplate<String, UserCreatedRequest> kafkaTemplate;

    public void sendUserCreatedEvent(UserCreatedRequest request) {
        log.info("Sending user created event: <{}>", request);
        Message<UserCreatedRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(TOPIC, "user-created-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
