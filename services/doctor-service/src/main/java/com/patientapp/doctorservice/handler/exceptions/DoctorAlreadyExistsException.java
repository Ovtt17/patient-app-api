package com.patientapp.doctorservice.handler.exceptions;

public class DoctorAlreadyExistsException extends RuntimeException {
  public DoctorAlreadyExistsException(String message) {
    super(message);
  }
}
