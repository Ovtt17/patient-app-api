package com.patientapp.doctorservice.common.handler.exceptions;

public class DoctorAlreadyExistsException extends RuntimeException {
  public DoctorAlreadyExistsException(String message) {
    super(message);
  }
}
