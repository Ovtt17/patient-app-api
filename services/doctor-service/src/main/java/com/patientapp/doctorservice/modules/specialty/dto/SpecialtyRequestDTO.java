package com.patientapp.doctorservice.modules.specialty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SpecialtyRequestDTO(
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(
                min = 3,
                max = 100,
                message = "El nombre debe tener entre 3 y 100 caracteres."
        )
        String name,
        @Size(
                max = 255,
                message = "La descripción no debe exceder los 255 caracteres."
        )
        String description
) {
}
