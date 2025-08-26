package com.patientapp.doctorservice.handler.exceptions;

public class SpecialtyAlreadyExistsException extends RuntimeException {
  public SpecialtyAlreadyExistsException(String message) {
    super(message);
  }
}
