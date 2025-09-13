package com.patientapp.doctorservice.modules.doctor.service.interfaces;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorUnavailabilityRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorUnavailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.DoctorUnavailability;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing doctor unavailability periods.
 * Provides methods to create, retrieve, and delete unavailability records for doctors.
 */
public interface DoctorUnavailabilityService {
    /**
     * Creates a new doctor unavailability record.
     * @param request the request DTO containing unavailability details
     * @return the created unavailability response DTO
     */
    DoctorUnavailabilityResponseDTO create(DoctorUnavailabilityRequestDTO request);

    /**
     * Retrieves all unavailability records for a specific doctor by their ID.
     * @param doctorId the UUID of the doctor
     * @return a list of unavailability response DTOs
     */
    List<DoctorUnavailabilityResponseDTO> getByDoctorId(UUID doctorId);

    /**
     * Retrieves all unavailability entities for a specific doctor by their ID.
     * @param doctorId the UUID of the doctor
     * @return a list of DoctorUnavailability entities
     */
    List<DoctorUnavailability> getAllEntitiesByDoctorId(UUID doctorId);

    /**
     * Deletes a doctor unavailability record by its ID.
     * @param id the ID of the unavailability record
     */
    void delete(Integer id);

    /**
     * Retrieves all unavailability entities for a specific doctor by their ID or throws an exception if none found.
     * @param doctorId the UUID of the doctor
     * @return a list of DoctorUnavailability entities
     * @throws RuntimeException if no unavailability records are found for the doctor
     */
    List<DoctorUnavailability> getAllByDoctorIdOrThrow(UUID doctorId);
}
