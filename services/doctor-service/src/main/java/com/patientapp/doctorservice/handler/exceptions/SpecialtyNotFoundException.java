package com.patientapp.doctorservice.handler.exceptions;

public class SpecialtyNotFoundException extends RuntimeException {
    public SpecialtyNotFoundException(String message) {
        super(message);
    }
}
