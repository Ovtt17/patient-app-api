package com.patientapp.appointmentservice.modules.appointment.service.interfaces;

import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentFilterDTO;
import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentRequestDTO;
import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentResponseDTO;
import com.patientapp.appointmentservice.modules.appointment.entity.Appointment;
import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
import com.patientapp.appointmentservice.modules.dashboard.dto.AppointmentSummary;
import com.patientapp.appointmentservice.modules.dashboard.dto.DoctorSummary;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    Long updateStatus(Long appointmentId, AppointmentStatus status);

    /**
     * Cancels an appointment, setting its status and cancellation metadata.
     *
     * @param appointmentId ID of the appointment to cancel
     */
    void cancel(Long appointmentId);

    /**
     * Retrieves a map of appointment counts for a specific doctor by date within a given month and year.
     *
     * @param doctorId UUID of the doctor
     * @param year the year to filter appointments
     * @param month the month to filter appointments
     * @return Map with LocalDate as key and count of appointments as value
     */
    Map<LocalDate, Long> getAppointmentCountByDoctorAndMonth(UUID doctorId, int year, int month);

    List<Instant> getAppointmentsByDoctorAndDay(UUID doctorId, LocalDate date);

    /**
     * Counts the total number of appointments.
     *
     * @return total count of appointments
     */
    Long countAllByCurrentMonth();

    /**
     * Counts the total number of completed appointments in the current month.
     *
     * @return count of completed appointments
     */
    Long countAllByCurrentMonthAndCompleted();

    /**
     * Counts the total number of cancelled appointments in the current month.
     *
     * @return count of cancelled appointments
     */
    Long countAllByCurrentMonthAndCancelled();

    /**
     * Retrieves a list of recent appointment summaries.
     *
     * @return List of {@link AppointmentSummary} for recent appointments
     */
    List<AppointmentSummary> findRecentAppointments();

    List<DoctorSummary> findTopActiveDoctors();

    List<Integer> getMonthlyAppointments();
}
