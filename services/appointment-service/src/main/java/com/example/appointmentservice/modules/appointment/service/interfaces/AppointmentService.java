package com.example.appointmentservice.modules.appointment.service.interfaces;

import com.example.appointmentservice.modules.appointment.dto.AppointmentFilterDTO;
import com.example.appointmentservice.modules.appointment.dto.AppointmentRequestDTO;
import com.example.appointmentservice.modules.appointment.dto.AppointmentResponseDTO;
import com.example.appointmentservice.modules.appointment.entity.Appointment;
import com.example.appointmentservice.modules.appointment.enums.AppointmentStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing appointments.
 */
public interface AppointmentService {

    /**
     * Creates a new appointment with the provided request data.
     *
     * @param request DTO containing appointment details
     * @return {@link AppointmentResponseDTO} of the created appointment
     */
    AppointmentResponseDTO create(AppointmentRequestDTO request);

    /**
     * Retrieves an appointment by its unique ID.
     *
     * @param appointmentId ID of the appointment
     * @return {@link AppointmentResponseDTO} with the given ID
     * @throws RuntimeException if the appointment does not exist
     */
    AppointmentResponseDTO getById(Long appointmentId);

    /**
     * Retrieves an Appointment entity by its ID or throws an exception if not found.
     *
     * @param appointmentId ID of the appointment
     * @return {@link Appointment} entity with the given ID
     * @throws RuntimeException if the appointment does not exist
     */
    Appointment getEntityByIdOrThrow(Long appointmentId);

    /**
     * Retrieves all appointments for a specific doctor after a given date.
     *
     * @param doctorId UUID of the doctor
     * @param fromDate start date for filtering appointments
     * @return List of {@link AppointmentResponseDTO} for the doctor
     */
    List<AppointmentResponseDTO> getAllByDoctor(UUID doctorId, Instant fromDate);

    /**
     * Retrieves all appointments for a specific patient after a given date.
     *
     * @param patientId UUID of the patient
     * @param fromDate start date for filtering appointments
     * @return List of {@link AppointmentResponseDTO} for the patient
     */
    List<AppointmentResponseDTO> getAllByPatient(UUID patientId, Instant fromDate);

    /**
     * Retrieves all appointments matching the provided filter criteria.
     *
     * @param filter DTO containing filter parameters
     * @return List of {@link AppointmentResponseDTO} matching the filter
     */
    List<AppointmentResponseDTO> getAllFiltered(AppointmentFilterDTO filter);

    /**
     * Updates the status of an appointment.
     *
     * @param appointmentId ID of the appointment
     * @param status new {@link AppointmentStatus} to set
     */
    void updateStatus(Long appointmentId, AppointmentStatus status);

    /**
     * Cancels an appointment, setting its status and cancellation metadata.
     *
     * @param appointmentId ID of the appointment to cancel
     */
    void cancel(Long appointmentId);
}
