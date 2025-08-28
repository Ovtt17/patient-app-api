package com.patientapp.doctorservice.handler;

import com.patientapp.doctorservice.handler.exceptions.DoctorAlreadyExistsException;
import com.patientapp.doctorservice.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.handler.exceptions.SpecialtyAlreadyExistsException;
import com.patientapp.doctorservice.handler.exceptions.SpecialtyNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.patientapp.doctorservice.handler.BusinessErrorCodes.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
}