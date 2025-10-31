package com.patientapp.doctorservice.modules.doctor.service.impl;

import com.patientapp.doctorservice.common.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.common.handler.exceptions.SpecialtyNotFoundException;
import com.patientapp.doctorservice.modules.doctor.dto.*;
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UUID create(DoctorRequestDTO request) {
        ZoneId zone = ZoneId.of("America/Managua");
        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setZoneId(zone.getId());
        return doctorRepository.save(doctor).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoctorPagedResponseDTO getAllActivePaged(
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
                .map(doctorMapper::toDoctorResponse)
                .toList();

        return DoctorPagedResponseDTO.builder()
                .content(doctorDTOs)
                .page(doctors.getNumber())
                .totalPages(doctors.getTotalPages())
                .totalElements(doctors.getTotalElements())
                .build();
    }

    @Override
    public List<DoctorResponseDTO> getAllActive() {
        List<Doctor> doctors = doctorRepository.findAllByActiveTrue();
        if (doctors.isEmpty()) {
            return Collections.emptyList();
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
        return doctorRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor no encontrado"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBasicInfo(UUID userId, DoctorBasicInfoDTO request) {
        Doctor doctor = getEntityByUserIdOrThrow(userId);
        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setPhone(request.phone());
        doctor.setGender(request.gender());

        doctorRepository.save(doctor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DoctorResponseDTO updateMedicalInfo(UUID userId, DoctorMedicalInfoDTO request) {
        Doctor doctor = getEntityByUserIdOrThrow(userId);
        doctor.setMedicalLicense(request.medicalLicense().trim());
        doctor.setOfficeNumber(request.officeNumber().trim());

        Set<Specialty> specialties = getValidatedSpecialties(request.specialtyIds());
        doctor.setSpecialties(specialties);

        if(request.appointmentDuration() != null) {
            doctor.setAppointmentDuration(request.appointmentDuration());
        }

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
        Doctor doctor = getEntityByUserIdOrThrow(userId);
        return doctorMapper.toDoctorResponse(doctor);
    }

    @Override
    public Doctor getEntityByUserIdOrThrow(UUID userId) {
        return doctorRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor no encontrado para el usuario dado."));
    }

    private Set<Specialty> getValidatedSpecialties(Set<Integer> specialtyIds) {
        if (specialtyIds == null || specialtyIds.isEmpty()) return new HashSet<>();
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
