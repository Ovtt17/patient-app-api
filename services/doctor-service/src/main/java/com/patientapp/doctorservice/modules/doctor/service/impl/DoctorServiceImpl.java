package com.patientapp.doctorservice.modules.doctor.service.impl;

import com.patientapp.doctorservice.common.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.common.handler.exceptions.SpecialtyNotFoundException;
import com.patientapp.doctorservice.modules.auth.client.AuthClient;
import com.patientapp.doctorservice.modules.auth.dto.UserResponseDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorPagedResponseDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.mapper.DoctorMapper;
import com.patientapp.doctorservice.modules.doctor.repository.DoctorRepository;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorService;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import com.patientapp.doctorservice.modules.specialty.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final SpecialtyService specialtyService;
    private final AuthClient authClient;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UUID create(UUID userId) {
        // TODO: Use actual zone from doctor's profile or request
        ZoneId zone = ZoneId.of("America/Managua");
        Doctor doctor = Doctor.builder()
                .userId(userId)
                .zoneId(zone.getId())
                .active(true)
                .build();
        return doctorRepository.save(doctor).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoctorPagedResponseDTO getAllActive(
            int page,
            int size,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = sortOrder.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Doctor> doctors = doctorRepository.findAllByActiveTrue(pageable);
        if (doctors.isEmpty()) {
            return null;
        }

        List<DoctorResponseDTO> doctorDTOs = doctors.stream()
                .map(doctor -> {
                    UserResponseDTO user = authClient.getUserById(doctor.getUserId());
                    return doctorMapper.toDoctorResponse(doctor, user);
                })
                .toList();

        return DoctorPagedResponseDTO.builder()
                .doctors(doctorDTOs)
                .page(doctors.getNumber())
                .totalPages(doctors.getTotalPages())
                .totalElements(doctors.getTotalElements())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoctorResponseDTO getById(UUID id) {
        Doctor doctor = getEntityByIdOrThrow(id);
        return getByUserId(doctor.getUserId());
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

    @Override
    public DoctorResponseDTO getByUserId(UUID userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor no encontrado para el usuario dado."));

        UserResponseDTO user = authClient.getUserById(userId);
        return doctorMapper.toDoctorResponse(doctor, user);
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
