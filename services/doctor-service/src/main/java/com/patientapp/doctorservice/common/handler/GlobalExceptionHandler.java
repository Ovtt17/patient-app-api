package com.patientapp.doctorservice.common.handler;

import com.patientapp.doctorservice.common.handler.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.patientapp.doctorservice.common.handler.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleLockedException(LockedException e) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ACCOUNT_LOCKED.getCode())
                                .businessErrorDescription(ACCOUNT_LOCKED.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleDisabledException(DisabledException e) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ACCOUNT_DISABLED.getCode())
                                .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BAD_CREDENTIALS.getCode())
                                .businessErrorDescription(BAD_CREDENTIALS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Set<String> errors = new HashSet<>();
        e.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception e) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription("Ha ocurrido un error inesperado, contacta con el administrador del sistema.")
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(NO_CODE.getCode())
                                .businessErrorDescription(NO_CODE.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity
                .status(USER_UNAUTHORIZED.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(USER_UNAUTHORIZED.getCode())
                                .businessErrorDescription(USER_UNAUTHORIZED.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ExceptionResponse> handleUnsupportedOperationException(UnsupportedOperationException e) {
        return ResponseEntity
                .status(UNSUPPORTED_AUTHENTICATION.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(UNSUPPORTED_AUTHENTICATION.getCode())
                                .businessErrorDescription(UNSUPPORTED_AUTHENTICATION.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    // Doctor no encontrado
    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleDoctorNotFoundException(DoctorNotFoundException e) {
        return ResponseEntity
                .status(DOCTOR_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(DOCTOR_NOT_FOUND.getCode())
                                .businessErrorDescription(DOCTOR_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    // Doctor ya existe
    @ExceptionHandler(DoctorAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleDoctorAlreadyExistsException(DoctorAlreadyExistsException e) {
        return ResponseEntity
                .status(DOCTOR_ALREADY_EXISTS.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(DOCTOR_ALREADY_EXISTS.getCode())
                                .businessErrorDescription(DOCTOR_ALREADY_EXISTS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    // Especialidad no encontrada
    @ExceptionHandler(SpecialtyNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleSpecialtyNotFoundException(SpecialtyNotFoundException e) {
        return ResponseEntity
                .status(SPECIALTY_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(SPECIALTY_NOT_FOUND.getCode())
                                .businessErrorDescription(SPECIALTY_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    // Especialidad ya existe
    @ExceptionHandler(SpecialtyAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleSpecialtyAlreadyExistsException(SpecialtyAlreadyExistsException e) {
        return ResponseEntity
                .status(SPECIALTY_ALREADY_EXISTS.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(SPECIALTY_ALREADY_EXISTS.getCode())
                                .businessErrorDescription(SPECIALTY_ALREADY_EXISTS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    // horario no encontrado
    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleScheduleNotFoundException(ScheduleNotFoundException e) {
        return ResponseEntity
                .status(SCHEDULE_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(SCHEDULE_NOT_FOUND.getCode())
                                .businessErrorDescription(SCHEDULE_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    // Conflicto de horario
    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<ExceptionResponse> handleScheduleConflictException(ScheduleConflictException e) {
        return ResponseEntity
                .status(SCHEDULE_CONFLICT.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(SCHEDULE_CONFLICT.getCode())
                                .businessErrorDescription(SCHEDULE_CONFLICT.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
}