package com.patientapp.doctorservice.modules.doctor.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record DoctorMedicalInfoDTO(
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

        @Positive(message = "La duración de la cita debe ser positiva.")
        Integer appointmentDuration
) {
}
