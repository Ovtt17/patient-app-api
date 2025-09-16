package com.patientapp.notificationservice.channel.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EmailTemplates {
    ACCOUNT_ACTIVATION("account-activation.html", "Activación de cuenta"),
    TEMP_PASSWORD("temporary-password.html", "Contraseña temporal");

    @Getter
    private final String template;
    @Getter
    private final String subject;
}
