package com.patientapp.patientservice.modules.patient.service.interfaces;

import com.patientapp.patientservice.modules.patient.dto.*;
import com.patientapp.patientservice.modules.patient.entity.Patient;

import java.util.UUID;

/**
 * Service interface for managing Patient entities.
 * Provides methods for creating, retrieving, updating, and deactivating content.
 */
public interface PatientService {
    /**
     * Creates a new patient.
     * @param request DTO containing patient data.
     * @return UUID of the created patient.
     */
    UUID create(PatientRequestDTO request);

    /**
     * Retrieves a paginated and sorted list of all active content.
     * @param page      zero-based page index to retrieve
     * @param size      number of records per page
     * @param sortBy    field name to sort by
     * @param sortOrder sort direction ("asc" or "desc")
     * @return {@link PatientPagedResponseDTO} containing the requested page of active content
     */
    PatientPagedResponseDTO getAllActive(
            int page,
            int size,
            String sortBy,
            String sortOrder
    );

    /**
     * Retrieves a patient by their ID.
     * @param id UUID of the patient.
     * @return PatientResponseDTO of the found patient.
     */
    PatientResponseDTO getById(UUID id);

    /**
     * Retrieves the Patient entity by ID or throws an exception if not found.
     * @param id UUID of the patient.
     * @return Patient entity.
     */
    Patient getEntityByIdOrThrow(UUID id);

    /**
     * Updates the basic information of an existing patient.
     *
     * @param userId  UUID of the user associated with the patient.
     * @param request DTO containing updated basic patient data.
     */
    void updateBasicInfo(UUID userId, PatientBasicInfoDTO request);

    /**
     * Updates an existing patient.
     * @param id UUID of the patient to update.
     * @param request DTO containing updated patient data.
     * @return Updated PatientResponseDTO.
     */
    PatientResponseDTO updateMedicalInfo(UUID id, PatientMedicalInfoDTO request);

    /**
     * Deactivates a patient by their ID.
     * @param id UUID of the patient to deactivate.
     */
    void deactivate(UUID id);

    /**
     * Retrieves a patient by their associated user ID.
     * @param userId UUID of the user.
     * @return PatientResponseDTO of the found patient.
     */
    PatientResponseDTO getByUserId(UUID userId);

    /**
     * Retrieves the Patient entity by user ID or throws an exception if not found.
     * @param userId UUID of the user.
     * @return Patient entity.
     */
    Patient getEntityByUserIdOrThrow(UUID userId);
}
