package com.patientapp.doctorservice.doctor.service.impl;

import com.patientapp.doctorservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.doctor.entity.Doctor;
import com.patientapp.doctorservice.handler.exceptions.DoctorAlreadyExistsException;
import com.patientapp.doctorservice.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.handler.exceptions.EmailAlreadyInUseException;
import com.patientapp.doctorservice.handler.exceptions.SpecialtyNotFoundException;
import com.patientapp.doctorservice.specialty.entity.Specialty;
import com.patientapp.doctorservice.doctor.mapper.DoctorMapper;
import com.patientapp.doctorservice.doctor.repository.DoctorRepository;
import com.patientapp.doctorservice.specialty.repository.SpecialtyRepository;
import com.patientapp.doctorservice.doctor.service.interfaces.DoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final DoctorMapper doctorMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Doctor create(DoctorRequestDTO request) {
        validateEmail(request.email());
        validateUserId(request.userId());
        Set<Specialty> specialties = fetchSpecialties(request.specialtyIds());

        Doctor doctor = doctorMapper.toEntity(request, new ArrayList<>(specialties));
        return doctorRepository.save(doctor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Doctor> getAllActive() {
        return doctorRepository.findByActiveTrue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Doctor getById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor no encontrado"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Doctor update(UUID id, DoctorRequestDTO request) {
        Doctor doctor = getById(id);

        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setMedicalLicense(request.medicalLicense());
        doctor.setOfficeNumber(request.officeNumber());

        Set<Specialty> specialties = fetchSpecialties(request.specialtyIds());
        doctor.setSpecialties(specialties);

        return doctorRepository.save(doctor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deactivate(UUID id) {
        Doctor doctor = getById(id);
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }

    /**
     * Validates that the email is not already registered for another doctor.
     *
     * @param email The email to check
     */
    private void validateEmail(String email) {
        if (doctorRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyInUseException("El correo electrónico ya está registrado para otro doctor");
        }
    }

    /**
     * Validates that the userId is not already linked to another doctor.
     *
     * @param userId The userId to check
     */
    private void validateUserId(UUID userId) {
        if (doctorRepository.findByUserId(userId).isPresent()) {
            throw new DoctorAlreadyExistsException("Ya existe un doctor vinculado a este usuario");
        }
    }

    /**
     * Fetches specialties by their IDs and validates they exist.
     *
     * @param specialtyIds Set of specialty IDs
     * @return Set of Specialty entities
     */
    private Set<Specialty> fetchSpecialties(Set<Integer> specialtyIds) {
        List<Specialty> specialties = specialtyRepository.findByIdIn(new ArrayList<>(specialtyIds));
        if (specialties.size() != specialtyIds.size()) {
            throw new SpecialtyNotFoundException("Una o más especialidades no fueron encontradas");
        }
        return new HashSet<>(specialties);
    }
}
