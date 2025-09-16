package com.patientapp.notificationservice.notification.entity;

import com.patientapp.notificationservice.kafka.consumer.appointment.AppointmentCreatedEvent;
import com.patientapp.notificationservice.kafka.consumer.auth.TemporaryPasswordEvent;
import com.patientapp.notificationservice.kafka.consumer.auth.UserCreatedEvent;
import com.patientapp.notificationservice.notification.enums.NotificationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private NotificationType type;
    private Instant notificationDate;
    private boolean sent;
    private UserCreatedEvent userCreatedEvent;
    private TemporaryPasswordEvent temporaryPasswordEvent;
    private AppointmentCreatedEvent appointmentCreatedEvent;
}
