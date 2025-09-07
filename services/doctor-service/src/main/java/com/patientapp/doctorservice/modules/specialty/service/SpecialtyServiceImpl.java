package com.patientapp.doctorservice.modules.specialty.service;

import com.patientapp.doctorservice.common.handler.exceptions.SpecialtyNotFoundException;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyRequestDTO;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyResponseDTO;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import com.patientapp.doctorservice.modules.specialty.mapper.SpecialtyMapper;
import com.patientapp.doctorservice.modules.specialty.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyMapper mapper;
    private final SpecialtyRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public SpecialtyResponseDTO create(SpecialtyRequestDTO request) {
        Specialty specialty = mapper.toEntity(request);
        Specialty savedSpecialty = save(specialty);
        return mapper.toResponseDTO(savedSpecialty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SpecialtyResponseDTO> getAll() {
        List<Specialty> specialties = repository.findAll();
        return specialties
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpecialtyResponseDTO getById(Integer id) {
        Specialty specialty = findByIdOrThrow(id);
        return mapper.toResponseDTO(specialty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public SpecialtyResponseDTO update(Integer id, SpecialtyRequestDTO request) {
        Specialty existingSpecialty = findByIdOrThrow(id);
        Specialty updatedSpecialty = mapper.update(request, existingSpecialty);
        Specialty savedSpecialty = save(updatedSpecialty);
        return mapper.toResponseDTO(savedSpecialty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String delete(Integer id) {
        Specialty specialty = findByIdOrThrow(id);
        String specialtyName = specialty.getName();
        repository.delete(specialty);
        return "Se ha eliminado la especialidad: " + specialtyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specialty save(Specialty specialty) {
        return repository.save(specialty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specialty findByIdOrThrow(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new SpecialtyNotFoundException("Especialidad no encontrada."));
    }

    @Override
    public List<Specialty> findByIdIn(List<Integer> specialtyIds) {
        return repository.findByIdIn(specialtyIds);
    }
}
