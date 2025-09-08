package com.patientapp.patientservice.modules.patient.service.impl;

import com.patientapp.patientservice.common.handler.exceptions.PatientNotFoundException;
import com.patientapp.patientservice.modules.auth.client.AuthClient;
import com.patientapp.patientservice.modules.auth.dto.UserResponseDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientPagedResponseDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientRequestDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientResponseDTO;
import com.patientapp.patientservice.modules.patient.entity.Patient;
import com.patientapp.patientservice.modules.patient.mapper.PatientMapper;
import com.patientapp.patientservice.modules.patient.repository.PatientRepository;
import com.patientapp.patientservice.modules.patient.service.interfaces.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;
    private final AuthClient authClient;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UUID create(UUID userId) {
        Patient patient = new Patient();
        patient.setUserId(userId);
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
        return mapper.toPatientPagedResponseDTO(patients);
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
    public PatientResponseDTO update(UUID id, PatientRequestDTO request) {
        Patient patient = getEntityByIdOrThrow(id);
        patient.setWeight(request.weight());
        patient.setHeight(request.height());
        patient.setBirthDate(request.birthDate());
        patient.setNotes(request.notes().trim());

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
        Patient patient = repository.findByUserId(userId)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado para el usuario dado."));

        UserResponseDTO user = authClient.getUserById(userId);
        return mapper.toPatientResponse(patient, user);
    }
}
