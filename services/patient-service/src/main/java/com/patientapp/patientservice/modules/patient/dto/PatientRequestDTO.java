package com.patientapp.patientservice.modules.patient.dto;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record PatientRequestDTO(
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(
                min = 2,
                max = 100,
                message = "El nombre debe tener entre 2 y 100 caracteres."
        )
        String firstName,

        @NotBlank(message = "El apellido es obligatorio.")
        @Size(
                min = 2,
                max = 100,
                message = "El apellido debe tener entre 2 y 100 caracteres."
        )
        String lastName,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "Debe ingresar un email válido.")
        String email,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Pattern(
                regexp = "^\\d{1,8}$",
                message = "El número de teléfono debe tener hasta 8 dígitos."
        )
        String phone,

        @NotNull(message = "El ID de usuario es obligatorio.")
        UUID userId
) {
}
