package com.patientapp.notificationservice.channel.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.patientapp.notificationservice.channel.email.EmailTemplates.ACCOUNT_ACTIVATION;

@Service
public class AuthEmailService extends EmailService{
    public AuthEmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Async
    public CompletableFuture<Boolean> sendAccountActivationEmail(
            String destinationEmail,
            String firstName,
            String activationCode,
            String confirmationUrl
    ) {
        Map<String, Object> variables = Map.of(
                "firstName", firstName,
                "activationCode", activationCode,
                "confirmationUrl", confirmationUrl
        );

        return sendEmail(
                destinationEmail,
                ACCOUNT_ACTIVATION.getTemplate(),
                ACCOUNT_ACTIVATION.getSubject(),
                variables
        );
    }
}
