package com.patientapp.notificationservice.kafka.consumer.appointment;

import com.patientapp.notificationservice.notification.handler.AppointmentEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentEventConsumer {
    private final AppointmentEventHandler appointmentEventHandler;

    @KafkaListener(topics = "appointment-created-topic", groupId = "appointmentGroup")
    public void consumeAppointmentCreatedEvent(AppointmentCreatedEvent event) {
        log.info("Received appointment created event for appointment ID: {}", event.appointmentId());
        appointmentEventHandler.handleAppointmentCreated(event);
    }
}
