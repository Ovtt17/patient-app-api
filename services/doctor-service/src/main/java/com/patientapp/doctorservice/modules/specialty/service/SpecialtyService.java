package com.patientapp.doctorservice.modules.specialty.service;

import com.patientapp.doctorservice.common.handler.exceptions.SpecialtyNotFoundException;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyRequestDTO;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyResponseDTO;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;

import java.util.List;

/**
 * Service interface for managing medical specialties.
 */
public interface SpecialtyService {
    /**
     * Creates a new specialty.
     *
     * @param request SpecialtyRequestDTO containing the name and optional description
     * @return SpecialtyResponseDTO for the created specialty
     */
    SpecialtyResponseDTO create(SpecialtyRequestDTO request);

    /**
     * Retrieves all specialties.
     *
     * @return List of SpecialtyResponseDTOs
     */
    List<SpecialtyResponseDTO> getAll();

    /**
     * Retrieves a specialty by its ID.
     *
     * @param id Integer ID of the specialty
     * @return SpecialtyResponseDTO with the given ID
     * @throws SpecialtyNotFoundException if the specialty does not exist
     */
    SpecialtyResponseDTO getById(Integer id);

    /**
     * Updates an existing specialty.
     *
     * @param id Integer ID of the specialty to update
     * @param request SpecialtyRequestDTO containing the updated fields
     * @return The updated SpecialtyResponseDTO
     * @throws SpecialtyNotFoundException if the specialty does not exist
     */
    SpecialtyResponseDTO update(Integer id, SpecialtyRequestDTO request);

    /**
     * Deletes a specialty by its ID.
     *
     * @param id Integer ID of the specialty to delete
     * @return A confirmation message
     * @throws SpecialtyNotFoundException if the specialty does not exist
     */
    String delete(Integer id);

    /**
     * Persists a specialty entity.
     *
     * @param specialty Specialty entity to save
     * @return The saved Specialty entity
     */
    Specialty save(Specialty specialty);

    /**
     * Retrieves a Specialty entity by ID or throws if not found.
     *
     * @param id Integer ID of the specialty
     * @return Specialty entity with the given ID
     * @throws SpecialtyNotFoundException if the specialty does not exist
     */
    Specialty findByIdOrThrow(Integer id);

    /**
     * Finds specialties by a list of IDs.
     *
     * @param specialtyIds List of Integer IDs
     * @return List of Specialty entities matching the IDs
     */
    List<Specialty> findByIdIn(List<Integer> specialtyIds);
}
