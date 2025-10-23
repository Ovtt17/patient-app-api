package com.patientapp.authservice.modules.user.dto;

import com.patientapp.authservice.modules.user.enums.Gender;
import jakarta.validation.constraints.*;

public record UserRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 3, max = 100, message = "El apellido debe tener entre 3 y 100 caracteres")
        String lastName,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Pattern(
                regexp = "^\\d{1,8}$",
                message = "El número de teléfono debe tener hasta 8 dígitos."
        )
        String phone,

        @NotNull(message = "El género es obligatorio")
        Gender gender,

        String bio
) {}