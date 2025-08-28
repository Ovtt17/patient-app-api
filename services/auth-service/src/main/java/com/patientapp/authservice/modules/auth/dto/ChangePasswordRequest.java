package com.patientapp.authservice.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String oldPassword,
        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
        String newPassword,
        @NotBlank(message = "La confirmación de la nueva contraseña es obligatoria")
        @Size(min = 8, message = "La confirmación de la nueva contraseña debe tener al menos 8 caracteres")
        String confirmNewPassword
) {
}
