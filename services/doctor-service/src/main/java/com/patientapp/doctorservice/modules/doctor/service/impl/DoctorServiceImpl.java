package com.patientapp.doctorservice.modules.doctor.service.impl;

import com.patientapp.doctorservice.common.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.common.handler.exceptions.SpecialtyNotFoundException;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.mapper.DoctorMapper;
import com.patientapp.doctorservice.modules.doctor.repository.DoctorRepository;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorService;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import com.patientapp.doctorservice.modules.specialty.service.SpecialtyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final SpecialtyService specialtyService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UUID create(UUID userId) {
        Doctor doctor = new Doctor();
        doctor.setUserId(userId);
        doctor.setActive(true);
        return doctorRepository.save(doctor).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DoctorResponseDTO> getAllActive() {
        List<Doctor> doctors = doctorRepository.findByActiveTrue();
        if (doctors.isEmpty()) {
            throw new DoctorNotFoundException("No se encontraron doctores activos");
        }
        return doctors.stream()
                .map(doctorMapper::toDoctorResponse)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoctorResponseDTO getById(UUID id) {
        Doctor doctor = getEntityByIdOrThrow(id);
        return doctorMapper.toDoctorResponse(doctor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Doctor getEntityByIdOrThrow(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor no encontrado"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DoctorResponseDTO update(UUID id, DoctorRequestDTO request) {
        Doctor doctor = getEntityByIdOrThrow(id);
        doctor.setMedicalLicense(request.medicalLicense().trim());
        doctor.setOfficeNumber(request.officeNumber().trim());

        Set<Specialty> specialties = getValidatedSpecialties(request.specialtyIds());
        doctor.setSpecialties(specialties);

        Doctor doctorUpdated = doctorRepository.save(doctor);
        return doctorMapper.toDoctorResponse(doctorUpdated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deactivate(UUID id) {
        Doctor doctor = getEntityByIdOrThrow(id);
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }

    private Set<Specialty> getValidatedSpecialties(Set<Integer> specialtyIds) {
        if (specialtyIds == null || specialtyIds.isEmpty()) return Set.of();

        return fetchSpecialties(specialtyIds);
    }

    /**
     * Fetches specialties by their IDs and validates they exist.
     *
     * @param specialtyIds Set of specialty IDs
     * @return Set of Specialty entities
     */
    private Set<Specialty> fetchSpecialties(Set<Integer> specialtyIds) {
        List<Specialty> specialties = specialtyService.findByIdIn(new ArrayList<>(specialtyIds));
        if (specialties.size() != specialtyIds.size()) {
            throw new SpecialtyNotFoundException("Una o más especialidades no fueron encontradas");
        }
        return new HashSet<>(specialties);
    }
}
