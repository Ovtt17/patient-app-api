package com.patientapp.notificationservice.channel.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EmailTemplates {
    ACCOUNT_ACTIVATION("account-activation.html", "Activación de cuenta"),
    TEMP_PASSWORD("temporary-password.html", "Contraseña temporal"),
    APPOINTMENT_CREATED_PATIENT("appointment-created-patient.html", "Cita agendada - Confirmación para paciente"),
    APPOINTMENT_CREATED_DOCTOR("appointment-created-doctor.html", "Nueva cita - Notificación para doctor"),
    ;

    @Getter
    private final String template;
    @Getter
    private final String subject;
}
