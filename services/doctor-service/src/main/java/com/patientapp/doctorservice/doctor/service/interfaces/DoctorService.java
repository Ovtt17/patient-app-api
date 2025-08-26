package com.patientapp.doctorservice.doctor.service.interfaces;

import com.patientapp.doctorservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.doctor.entity.Doctor;
import com.patientapp.doctorservice.handler.exceptions.DoctorAlreadyExistsException;
import com.patientapp.doctorservice.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.handler.exceptions.SpecialtyNotFoundException;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    /**
     * Creates a new doctor with the provided information and specialties.
     *
     * @param dto DoctorRequestDTO containing doctor's firstName, lastName, email, phone,
     *            medicalLicense, officeNumber, userId, and specialtyIds
     * @return The created Doctor entity
     * @throws DoctorAlreadyExistsException if a doctor with the same email or userId already exists
     * @throws DoctorAlreadyExistsException if a doctor with the same email or userId already exists
     * @throws SpecialtyNotFoundException if any specialtyId is invalid
     */
    Doctor create(DoctorRequestDTO dto);

    /**
     * Retrieves all active doctors.
     *
     * @return List of Doctor entities that are active
     */
    List<Doctor> getAllActive();

    /**
     * Retrieves a doctor by their unique ID.
     *
     * @param id UUID of the doctor
     * @return Doctor entity with the given ID
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    Doctor getById(UUID id);

    /**
     * Updates the information and specialties of an existing doctor.
     *
     * @param id  UUID of the doctor to update
     * @param dto DoctorRequestDTO containing the updated information and specialtyIds
     * @return The updated Doctor entity
     * @throws DoctorNotFoundException if the doctor does not exist
     * @throws SpecialtyNotFoundException if any specialtyId is invalid
     */
    Doctor update(UUID id, DoctorRequestDTO dto);

    /**
     * Deactivates a doctor instead of deleting them from the database.
     *
     * @param id UUID of the doctor to deactivate
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    void deactivate(UUID id);
}
