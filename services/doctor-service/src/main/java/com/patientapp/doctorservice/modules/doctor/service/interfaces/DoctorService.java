package com.patientapp.doctorservice.modules.doctor.service.interfaces;

import com.patientapp.doctorservice.common.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.common.handler.exceptions.SpecialtyNotFoundException;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    /**
     * Creates a new doctor associated with the given userId.
     *
     * @param userId UUID of the user associated with the doctor
     * @return ID of the created Doctor
     */
    UUID create(UUID userId);

    /**
     * Retrieves all active doctors.
     *
     * @return List of Doctors that are active
     */
    List<DoctorResponseDTO> getAllActive();

    /**
     * Retrieves a doctor by their unique ID.
     *
     * @param id UUID of the doctor
     * @return DoctorResponseDTO with the given ID
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    DoctorResponseDTO getById(UUID id);

    /**
     * Retrieves a Doctor entity by its ID or throws an exception if not found.
     *
     * @param id UUID of the doctor
     * @return Doctor entity with the given ID
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    Doctor getEntityByIdOrThrow(UUID id);

    /**
     * Updates the information and specialties of an existing doctor.
     *
     * @param id  UUID of the doctor to update
     * @param request DoctorRequestDTO containing the updated information and specialtyIds
     * @return The updated DoctorResponseDTO
     * @throws DoctorNotFoundException if the doctor does not exist
     * @throws SpecialtyNotFoundException if any specialtyId is invalid
     */
    DoctorResponseDTO update(UUID id, DoctorRequestDTO request);

    /**
     * Deactivates a doctor instead of deleting them from the database.
     *
     * @param id UUID of the doctor to deactivate
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    void deactivate(UUID id);
}
