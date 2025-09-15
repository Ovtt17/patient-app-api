package com.patientapp.notificationservice.channel.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EmailTemplates {
    EMAIL_VERIFICATION("email-verification.html", "Por favor verifica tu correo electrónico");

    @Getter
    private final String template;
    @Getter
    private final String subject;
}
