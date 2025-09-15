package com.patientapp.notificationservice.channel.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Value("${spring.mail.username}")
    private String emailFrom;

    /**
     * Send an email asynchronously using the specified template and variables.
     * @param destinationEmail The recipient's email address
     * @param templateName The name of the Thymeleaf template to use
     * @param subject The subject of the email
     * @param variables A map of variables to populate the template
     * @return CompletableFuture<Boolean> true if the email was sent successfully, false otherwise
     */
    public CompletableFuture<Boolean> sendEmail(
            String destinationEmail,
            String templateName,
            String subject,
            Map<String, Object> variables
    ) {
        return CompletableFuture.supplyAsync(() -> {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(
                        mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name()
                );
                messageHelper.setFrom(emailFrom);

                Context context = new Context();
                context.setVariables(variables);
                messageHelper.setSubject(subject);

                String htmlTemplate = templateEngine.process(templateName, context);
                messageHelper.setText(htmlTemplate, true);

                messageHelper.setTo(destinationEmail);
                mailSender.send(mimeMessage);

                log.info("Email sent successfully to {} with template {}", destinationEmail, templateName);
                return true;

            } catch (MessagingException e) {
                log.warn("WARNING - Failed to send email to {} with template {}: {}", destinationEmail, templateName, e.getMessage());
                return false;
            }
        }, executorService);
    }

}
