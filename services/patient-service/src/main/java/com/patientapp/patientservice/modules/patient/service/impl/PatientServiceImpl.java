package com.patientapp.patientservice.modules.patient.service.impl;

import com.patientapp.patientservice.common.handler.exceptions.PatientNotFoundException;
import com.patientapp.patientservice.common.utils.NullSafe;
import com.patientapp.patientservice.modules.patient.dto.*;
import com.patientapp.patientservice.modules.patient.entity.Patient;
import com.patientapp.patientservice.modules.patient.mapper.PatientMapper;
import com.patientapp.patientservice.modules.patient.repository.PatientRepository;
import com.patientapp.patientservice.modules.patient.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UUID create(PatientRequestDTO request) {
        Patient patient = mapper.toEntity(request);
        patient.setActive(true);
        return repository.save(patient).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PatientPagedResponseDTO getAllActive(
            int page,
            int size,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = sortOrder.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Patient> patients = repository.findAllByActiveTrue(pageable);
        if (patients.isEmpty()) {
            return null;
        }

        List<PatientResponseDTO> patientDTOs = patients.stream()
                .map(mapper::toPatientResponse)
                .toList();

        return PatientPagedResponseDTO.builder()
                .content(patientDTOs)
                .page(patients.getNumber())
                .totalPages(patients.getTotalPages())
                .totalElements(patients.getTotalElements())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PatientResponseDTO getById(UUID id) {
        Patient patient = getEntityByIdOrThrow(id);
        return mapper.toPatientResponse(patient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Patient getEntityByIdOrThrow(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateBasicInfo(UUID userId, PatientBasicInfoDTO request) {
        Patient patient = getEntityByUserIdOrThrow(userId);
        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setPhone(request.phone());
        patient.setGender(request.gender());

        repository.save(patient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public PatientResponseDTO updateMedicalInfo(UUID userId, PatientMedicalInfoDTO request) {
        Patient patient = getEntityByUserIdOrThrow(userId);
        patient.setWeight(request.weight());
        patient.setHeight(request.height());
        patient.setBirthDate(request.birthDate());
        patient.setNotes(NullSafe.ifNotBlankOrNull(request.notes()));

        Patient patientUpdated = repository.save(patient);
        return mapper.toPatientResponse(patientUpdated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deactivate(UUID id) {
        Patient patient = getEntityByIdOrThrow(id);
        // TODO: Block access in auth-service
        patient.setActive(false);
        repository.save(patient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PatientResponseDTO getByUserId(UUID userId) {
        Patient patient = getEntityByUserIdOrThrow(userId);
        return mapper.toPatientResponse(patient);
    }

    @Override
    public Patient getEntityByUserIdOrThrow(UUID userId) {
        return repository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado para el usuario dado."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PatientResponseDTO> getByIds(List<UUID> ids) {
        return repository.findAllById(ids).stream()
                .map(mapper::toPatientResponse)
                .toList();
    }
}
