package com.patientapp.patientservice.modules.patient.service.interfaces;

import com.patientapp.patientservice.modules.patient.dto.PatientPagedResponseDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientRequestDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientResponseDTO;
import com.patientapp.patientservice.modules.patient.entity.Patient;

import java.util.UUID;

/**
 * Service interface for managing Patient entities.
 * Provides methods for creating, retrieving, updating, and deactivating patients.
 */
public interface PatientService {
    /**
     * Creates a new patient.
     * @param userId UUID of the user associated with the patient.
     * @return UUID of the created patient.
     */
    UUID create(UUID userId);

    /**
     * Retrieves a paginated and sorted list of all active patients.
     * @param page      zero-based page index to retrieve
     * @param size      number of records per page
     * @param sortBy    field name to sort by
     * @param sortOrder sort direction ("asc" or "desc")
     * @return {@link PatientPagedResponseDTO} containing the requested page of active patients
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
     * Updates an existing patient.
     * @param id UUID of the patient to update.
     * @param request DTO containing updated patient data.
     * @return Updated PatientResponseDTO.
     */
    PatientResponseDTO update(UUID id, PatientRequestDTO request);

    /**
     * Deactivates a patient by their ID.
     * @param id UUID of the patient to deactivate.
     */
    void deactivate(UUID id);
}
