package com.patientapp.notificationservice.channel.email;

import com.patientapp.notificationservice.kafka.consumer.auth.TemporaryPasswordEvent;
import com.patientapp.notificationservice.kafka.consumer.auth.UserCreatedEvent;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.patientapp.notificationservice.channel.email.EmailTemplates.ACCOUNT_ACTIVATION;
import static com.patientapp.notificationservice.channel.email.EmailTemplates.TEMP_PASSWORD;

@Service
public class AuthEmailService extends EmailService{
    public AuthEmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Async
    public CompletableFuture<Boolean> sendAccountActivationEmail(UserCreatedEvent event) {
        Map<String, Object> variables = Map.of(
                "firstName", event.firstName(),
                "activationCode", event.activationCode(),
                "confirmationUrl", event.confirmationUrl()
        );

        return sendEmail(
                event.email(),
                ACCOUNT_ACTIVATION.getTemplate(),
                ACCOUNT_ACTIVATION.getSubject(),
                variables
        );
    }

    @Async
    public CompletableFuture<Boolean> sendTemporaryPasswordEmail(TemporaryPasswordEvent event) {
        Map<String, Object> variables = Map.of(
                "firstName", event.firstName(),
                "temporaryPassword", event.temporaryPassword(),
                "loginUrl", event.loginUrl()
        );
        return sendEmail(
                event.email(),
                TEMP_PASSWORD.getTemplate(),
                TEMP_PASSWORD.getSubject(),
                variables
        );
    }
}
