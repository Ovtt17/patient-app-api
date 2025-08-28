package com.patientapp.doctorservice.modules.doctor.dto;

import jakarta.validation.constraints.*;

import java.util.Set;
import java.util.UUID;

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

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "Debe ingresar un email válido.")
        String email,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Pattern(
                regexp = "^\\d{1,8}$",
                message = "El número de teléfono debe tener hasta 8 dígitos."
        )
        String phone,

        @Size(
                min = 3,
                max = 20,
                message = "La licencia médica debe tener entre 3 y 20 caracteres."
        )
        String medicalLicense,

        @Size(
                min = 1,
                max = 10,
                message = "El número de consultorio no puede ser mayor a 10 caracteres."
        )
        String officeNumber,

        Set<@Positive(message = "El ID de especialidad debe ser positivo.") Integer> specialtyIds,

        @NotNull(message = "El ID de usuario es obligatorio.")
        UUID userId
) {
}