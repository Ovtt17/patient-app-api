package com.patientapp.authservice.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 3, max = 100, message = "El apellido debe tener entre 3 y 100 caracteres")
        String lastName,

        @Email(message = "El email no es válido")
        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Pattern(
                regexp = "^\\d{1,8}$",
                message = "El número de teléfono debe tener hasta 8 dígitos."
        )
        String phone,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "La confirmación de la contraseña es obligatoria")
        @Size(min = 8, message = "La confirmación de la contraseña debe tener al menos 8 caracteres")
        String confirmPassword
) {
}

