package com.patientapp.notificationservice.channel.email;

import com.patientapp.notificationservice.kafka.consumer.appointment.AppointmentCreatedEvent;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class AppointmentEmailService extends EmailService {

    public AppointmentEmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    private String formatInstantForZone(Instant instant, String zoneIdStr) {
        ZoneId zone = (zoneIdStr != null) ? ZoneId.of(zoneIdStr) : ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd 'de' MMMM yyyy, hh:mm a")
                .withLocale(new Locale("es", "ES"))
                .withZone(zone);
        return formatter.format(instant);
    }

    @Async
    public CompletableFuture<Boolean> sendAppointmentCreatedEmailToPatient(AppointmentCreatedEvent event) {
        // TODO: Assign time zone based on patient's profile, for now using system default
        String formattedDateTime = formatInstantForZone(event.appointmentStart(), null);

        Map<String, Object> variables = Map.of(
                "appointmentId", event.appointmentId(),
                "patientName", event.patientName(),
                "doctorName", event.doctorName(),
                "formattedAppointmentDateTime", formattedDateTime
        );

        return sendEmail(
                event.patientEmail(),
                EmailTemplates.APPOINTMENT_CREATED_PATIENT.getTemplate(),
                EmailTemplates.APPOINTMENT_CREATED_PATIENT.getSubject(),
                variables
        );
    }

    @Async
    public CompletableFuture<Boolean> sendAppointmentCreatedEmailToDoctor(AppointmentCreatedEvent event) {
        String formattedDateTime = formatInstantForZone(event.appointmentStart(), event.doctorZoneId());

        Map<String, Object> variables = Map.of(
                "appointmentId", event.appointmentId(),
                "patientName", event.patientName(),
                "doctorName", event.doctorName(),
                "formattedAppointmentDateTime", formattedDateTime
        );

        return sendEmail(
                event.doctorEmail(),
                EmailTemplates.APPOINTMENT_CREATED_DOCTOR.getTemplate(),
                EmailTemplates.APPOINTMENT_CREATED_DOCTOR.getSubject(),
                variables
        );
    }
}
