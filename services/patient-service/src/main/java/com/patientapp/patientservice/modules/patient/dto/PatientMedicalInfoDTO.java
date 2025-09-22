package com.patientapp.patientservice.modules.patient.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientMedicalInfoDTO(
        @Positive(message = "El peso debe ser un valor positivo.")
        @Max(value = 500, message = "El peso no puede superar las 500 lb.")
        Double weight,

        @Positive(message = "La altura debe ser un valor positivo.")
        @Max(value = 300, message = "La altura no puede superar los 300 cm.")
        Double height,

        @Past(message = "La fecha de nacimiento debe ser anterior al día de hoy.")
        LocalDate birthDate,

        @Size(max = 1000, message = "Las notas no pueden superar los 1000 caracteres.")
        String notes
) {
}
