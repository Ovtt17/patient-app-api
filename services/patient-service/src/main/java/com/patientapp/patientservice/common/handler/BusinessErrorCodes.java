package com.patientapp.patientservice.common.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No se ha definido un código de error."),
    NEW_PASSWORD_DOES_NOT_MATCH (301, BAD_REQUEST, "Las contraseñas no coinciden. Por favor, verifica e intenta nuevamente."),
    ACCOUNT_LOCKED (302, FORBIDDEN, "La cuenta de usuario está bloqueada. Contacta al administrador para más información."),
    ACCOUNT_DISABLED (303, FORBIDDEN, "La cuenta de usuario está deshabilitada. Contacta al administrador para más información."),
    BAD_CREDENTIALS (304, FORBIDDEN, "Usuario o contraseña incorrectos. Verifica tus credenciales e intenta nuevamente."),
    UNSUPPORTED_AUTHENTICATION (304, FORBIDDEN, "Método de autenticación no soportado. Por favor, contacta al administrador."),
    ACCESS_DENIED (304, FORBIDDEN, "Acceso denegado. No tienes permiso para realizar esta acción."),

    PATIENT_NOT_FOUND(601, NOT_FOUND, "Paciente no encontrado."),
    PATIENT_ALREADY_EXISTS(602, CONFLICT, "El paciente ya existe."),
    EMAIL_ALREADY_IN_USE(603, CONFLICT, "El correo electrónico ya está en uso."),

    ILLEGAL_ARGUMENT (399, BAD_REQUEST, "Argumento ilegal proporcionado. Verifica la información e intenta nuevamente."),
    USER_UNAUTHORIZED(401, UNAUTHORIZED, "No tienes permiso para realizar esta acción. Contacta al administrador si crees que esto es un error."),
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
