package com.patientapp.notificationservice.channel.email;

import com.patientapp.notificationservice.kafka.consumer.medicalrecord.MedicalRecordCreatedEvent;
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
public class MedicalRecordEmailService extends EmailService {
    public MedicalRecordEmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    private String formatInstantForZone(Instant instant) {
        ZoneId zone = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd 'de' MMMM yyyy, hh:mm a")
                .withLocale(new Locale("es", "ES"))
                .withZone(zone);
        return formatter.format(instant);
    }

    @Async
    public CompletableFuture<Boolean> sendMedicalRecordCreatedEmail(MedicalRecordCreatedEvent event) {
        String formattedDate = formatInstantForZone(event.createdDate());

        Map<String, Object> variables = Map.ofEntries(
                Map.entry("patientName", event.patientName()),
                Map.entry("doctorName", event.doctorName()),
                Map.entry("appointmentId", event.appointmentId()),
                Map.entry("weight", event.weight()),
                Map.entry("height", event.height()),
                Map.entry("bloodType", event.bloodType()),
                Map.entry("allergies", event.allergies()),
                Map.entry("chronicDiseases", event.chronicDiseases()),
                Map.entry("medications", event.medications()),
                Map.entry("diagnostic", event.diagnostic()),
                Map.entry("createdDate", formattedDate)
        );

        return sendEmail(
                event.patientEmail(),
                EmailTemplates.MEDICAL_RECORD_CREATED.getTemplate(),
                EmailTemplates.MEDICAL_RECORD_CREATED.getSubject(),
                variables
        );
    }
}
