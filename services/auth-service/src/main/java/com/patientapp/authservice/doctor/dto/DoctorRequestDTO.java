package com.patientapp.authservice.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DoctorRequestDTO(
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

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(
                min = 3,
                max = 30,
                message = "El nombre de usuario debe tener entre 3 y 30 caracteres."
        )
        String username,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "Debe ingresar un email válido.")
        String email,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Pattern(
                regexp = "^\\d{1,8}$",
                message = "El número de teléfono debe tener hasta 8 dígitos."
        )
        String phone,
        UUID userId
) {
}
