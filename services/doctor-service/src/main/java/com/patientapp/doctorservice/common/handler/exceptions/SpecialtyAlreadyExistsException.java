package com.patientapp.doctorservice.common.handler.exceptions;

public class SpecialtyAlreadyExistsException extends RuntimeException {
  public SpecialtyAlreadyExistsException(String message) {
    super(message);
  }
}
