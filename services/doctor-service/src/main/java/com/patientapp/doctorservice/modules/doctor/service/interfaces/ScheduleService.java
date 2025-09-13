package com.patientapp.doctorservice.modules.doctor.service.interfaces;

import com.patientapp.doctorservice.common.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.common.handler.exceptions.ScheduleNotFoundException;
import com.patientapp.doctorservice.modules.doctor.dto.ScheduleRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.ScheduleResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Schedule;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing doctor schedules.
 * <p>
 * Provides methods for creating, retrieving, updating, and deleting schedules associated with doctors.
 * All methods are expected to handle validation and business rules, and throw appropriate exceptions when needed.
 * </p>
 */
public interface ScheduleService {
    /**
     * Creates a new schedule for a doctor.
     * <p>
     * The schedule must specify the doctor, day of week, and time range. Validation should ensure no overlap
     * with existing schedules for the same doctor and that the time range is valid.
     * </p>
     *
     * @param request DTO containing schedule details
     * @return The created ScheduleResponseDTO
     * @throws DoctorNotFoundException if the doctor does not exist
     * @throws IllegalArgumentException if the schedule overlaps or is invalid
     */
    ScheduleResponseDTO create(ScheduleRequestDTO request);

    /**
     * Gets a schedule by its unique ID.
     *
     * @param id The schedule's ID
     * @return ScheduleResponseDTO with the given ID
     * @throws ScheduleNotFoundException if not found
     */
    ScheduleResponseDTO getById(Integer id);

    /**
     * Retrieves a Schedule entity by its ID or throws an exception if not found.
     *
     * @param id The schedule's ID
     * @return Schedule entity with the given ID
     * @throws ScheduleNotFoundException if not found
     */
    Schedule getEntityByIdOrThrow(Integer id);

    /**
     * Gets all schedules for a doctor by their unique ID.
     * <p>
     * Returns all schedules associated with the specified doctor. Throws an exception if the doctor has no schedules.
     * </p>
     *
     * @param doctorId UUID of the doctor
     * @return List of Schedule entities for the doctor
     * @throws DoctorNotFoundException if the doctor does not exist
     * @throws ScheduleNotFoundException if the doctor has no schedules
     */
    List<Schedule> getAllEntitiesByDoctorIdOrThrow(UUID doctorId);

    /**
     * Gets all schedules for a doctor by their unique ID.
     * <p>
     * Returns all schedules associated with the specified doctor. May be empty if the doctor has no schedules.
     * </p>
     *
     * @param doctorId UUID of the doctor
     * @return List of ScheduleResponseDTOs for the doctor
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    List<ScheduleResponseDTO> getByDoctorId(UUID doctorId);

    /**
     * Gets all schedules for a doctor on a specific day of the week.
     * <p>
     * Returns schedules associated with the specified doctor that fall on the given day of the week.
     * May be empty if no schedules exist for that day.
     * </p>
     *
     * @param doctorId  UUID of the doctor
     * @param dayOfWeek Day of the week to filter schedules
     * @return List of ScheduleResponseDTOs for the doctor on the specified day
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    List<ScheduleResponseDTO> getByDoctorIdAndDay(UUID doctorId, DayOfWeek dayOfWeek);

    /**
     * Updates an existing schedule.
     * <p>
     * The schedule must exist. Validation should ensure no overlap with other schedules for the same doctor
     * and that the time range is valid.
     * </p>
     *
     * @param id      The schedule's ID to update
     * @param request DTO containing updated schedule details
     * @return The updated ScheduleResponseDTO
     * @throws ScheduleNotFoundException if not found
     * @throws IllegalArgumentException if the update is invalid
     */
    ScheduleResponseDTO update(Integer id, ScheduleRequestDTO request);

    /**
     * Deletes a schedule by its ID.
     * <p>
     * Removes the schedule from the system. If the schedule does not exist, an exception is thrown.
     * </p>
     *
     * @param id The schedule's ID to delete
     * @throws ScheduleNotFoundException if not found
     */
    void delete(Integer id);

    /**
     * Deletes all schedules associated with a specific doctor.
     * <p>
     * Removes all schedules for the given doctor. If the doctor does not exist, an exception is thrown.
     * </p>
     *
     * @param doctorId UUID of the doctor whose schedules should be deleted
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    void deleteAllByDoctorId(UUID doctorId);
}
