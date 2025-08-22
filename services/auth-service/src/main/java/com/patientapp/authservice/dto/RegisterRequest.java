package com.patientapp.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @Email (message = "El email no es válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Size(max = 8, message = "El número de teléfono no debe exceder los 8 caracteres")
    private String phone;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "La confirmación de la contraseña es obligatoria")
    @Size(min = 8, message = "La confirmación de la contraseña debe tener al menos 8 caracteres")
    private String confirmPassword;
}

