package com.patientapp.notificationservice.notification.handler;

import com.patientapp.notificationservice.channel.email.AppointmentEmailService;
import com.patientapp.notificationservice.kafka.consumer.appointment.AppointmentCreatedEvent;
import com.patientapp.notificationservice.notification.entity.Notification;
import com.patientapp.notificationservice.notification.enums.NotificationType;
import com.patientapp.notificationservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentEventHandler {
    private final NotificationService notificationService;
    private final AppointmentEmailService appointmentEmailService;

    public void handleAppointmentCreated(AppointmentCreatedEvent event) {
        log.info("Handling appointment created event: {}", event.appointmentId());
        handlePatientNotification(event);
        handleDoctorNotification(event);
    }

    private void handlePatientNotification(AppointmentCreatedEvent event) {
        log.info("Creating patient notification for appointment ID: {}", event.appointmentId());
        Notification notification = Notification.builder()
                .type(NotificationType.APPOINTMENT_CREATED_PATIENT)
                .notificationDate(Instant.now())
                .sent(false)
                .appointmentCreatedEvent(event)
                .build();
        notificationService.save(notification);

        CompletableFuture<Boolean> sentFuture = appointmentEmailService.sendAppointmentCreatedEmailToPatient(event);
        sentFuture.thenAccept(sent -> {
            if (sent) {
                notification.setSent(true);
                notificationService.save(notification);
                log.info("Appointment notification sent successfully to patient {}", event.patientEmail());
            } else {
                log.warn("Failed to send appointment notification to patient {}", event.patientEmail());
            }
        });
    }

    private void handleDoctorNotification(AppointmentCreatedEvent event) {
        log.info("Creating doctor notification for appointment ID: {}", event.appointmentId());
        Notification notification = Notification.builder()
                .type(NotificationType.APPOINTMENT_CREATED_DOCTOR)
                .notificationDate(Instant.now())
                .sent(false)
                .appointmentCreatedEvent(event)
                .build();
        notificationService.save(notification);

        CompletableFuture<Boolean> sentFuture = appointmentEmailService.sendAppointmentCreatedEmailToDoctor(event);
        sentFuture.thenAccept(sent -> {
            if (sent) {
                notification.setSent(true);
                notificationService.save(notification);
                log.info("Appointment notification sent successfully to doctor {}", event.doctorEmail());
            } else {
                log.warn("Failed to send appointment notification to doctor {}", event.doctorEmail());
            }
        });
    }
}
