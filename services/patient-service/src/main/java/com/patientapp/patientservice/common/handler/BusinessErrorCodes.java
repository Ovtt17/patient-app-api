package com.patientapp.patientservice.common.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No se ha definido un código de error."),

    PATIENT_NOT_FOUND(601, NOT_FOUND, "Paciente no encontrado."),
    PATIENT_ALREADY_EXISTS(602, CONFLICT, "El paciente ya existe."),
    EMAIL_ALREADY_IN_USE(603, CONFLICT, "El correo electrónico ya está en uso."),

    ILLEGAL_ARGUMENT (399, BAD_REQUEST, "Argumento ilegal proporcionado. Verifica la información e intenta nuevamente."),
    ENTITY_NOT_FOUND (451, NOT_FOUND, "Entidad no encontrada.")
    ;

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
