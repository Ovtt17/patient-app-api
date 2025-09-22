package com.patientapp.patientservice.modules.patient.dto;

import com.patientapp.patientservice.modules.patient.enums.Gender;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record PatientRequestDTO(
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

        @NotNull(message = "El género es obligatorio")
        Gender gender,

        String profilePictureUrl,

        @NotNull(message = "El ID de usuario es obligatorio.")
        UUID userId
) {
}
