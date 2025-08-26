package com.patientapp.doctorservice.doctor.service.interfaces;

import com.patientapp.doctorservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.doctor.entity.Doctor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface DoctorService {

    /**
     * Creates a new doctor with the provided information and specialties.
     *
     * @param dto DoctorRequestDTO containing doctor's firstName, lastName, email, phone,
     *            medicalLicense, officeNumber, userId, and specialtyIds
     * @return The created Doctor entity
     * @throws IllegalArgumentException if email or userId already exists, or if any specialtyId is invalid
     */
    Doctor createDoctor(DoctorRequestDTO dto);

    /**
     * Retrieves all active doctors.
     *
     * @return List of Doctor entities that are active
     */
    List<Doctor> getAllActiveDoctors();

    /**
     * Retrieves a doctor by their unique ID.
     *
     * @param id UUID of the doctor
     * @return Doctor entity with the given ID
     * @throws NoSuchElementException if the doctor does not exist
     */
    Doctor getDoctorById(UUID id);

    /**
     * Updates the information and specialties of an existing doctor.
     *
     * @param id  UUID of the doctor to update
     * @param dto DoctorRequestDTO containing the updated information and specialtyIds
     * @return The updated Doctor entity
     * @throws NoSuchElementException    if the doctor does not exist
     * @throws IllegalArgumentException if any specialtyId is invalid
     */
    Doctor updateDoctor(UUID id, DoctorRequestDTO dto);

    /**
     * Deactivates a doctor instead of deleting them from the database.
     *
     * @param id UUID of the doctor to deactivate
     * @throws NoSuchElementException if the doctor does not exist
     */
    void deactivateDoctor(UUID id);
}
