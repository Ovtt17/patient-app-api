package com.patientapp.authservice.common.handler.exceptions;

public class MustChangePasswordException extends RuntimeException {
    public MustChangePasswordException(String message) {
        super(message);
    }
}
