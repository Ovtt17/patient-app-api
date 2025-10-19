package com.patientapp.authservice.modules.auth.dto;

import com.patientapp.authservice.modules.role.enums.Roles;
import com.patientapp.authservice.modules.user.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterRequest(
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

        @NotNull(message = "El género es obligatorio")
        Gender gender,

        @NotNull(message = "El rol es obligatorio")
        Roles role,

        String profilePictureUrl,

        UUID userId
) {
}

