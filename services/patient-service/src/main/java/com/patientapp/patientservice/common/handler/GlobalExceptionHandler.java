package com.patientapp.patientservice.common.handler;


import com.patientapp.patientservice.common.handler.exceptions.PatientNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.patientapp.patientservice.common.handler.BusinessErrorCodes.PATIENT_NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Patient Not Found Exception
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleDoctorNotFoundException(PatientNotFoundException e) {
        return ResponseEntity
                .status(PATIENT_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(PATIENT_NOT_FOUND.getCode())
                                .businessErrorDescription(PATIENT_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
}