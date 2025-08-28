package com.patientapp.authservice.handler.exceptions;

public class MustChangePasswordException extends RuntimeException {
    public MustChangePasswordException(String message) {
        super(message);
    }
}
