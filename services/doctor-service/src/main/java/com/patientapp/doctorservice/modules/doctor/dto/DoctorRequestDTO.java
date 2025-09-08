package com.patientapp.doctorservice.modules.doctor.dto;

import jakarta.validation.constraints.*;

import java.util.Set;
import java.util.UUID;

public record DoctorRequestDTO(
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