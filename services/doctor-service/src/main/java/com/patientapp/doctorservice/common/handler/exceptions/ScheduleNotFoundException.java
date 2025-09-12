package com.patientapp.doctorservice.common.handler.exceptions;

public class ScheduleNotFoundException extends RuntimeException {
  public ScheduleNotFoundException(String message) {
    super(message);
  }
}
