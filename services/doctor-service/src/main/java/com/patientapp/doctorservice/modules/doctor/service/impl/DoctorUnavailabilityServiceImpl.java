package com.patientapp.doctorservice.modules.doctor.service.impl;

import com.patientapp.doctorservice.common.handler.exceptions.ScheduleConflictException;
import com.patientapp.doctorservice.common.handler.exceptions.ScheduleNotFoundException;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorUnavailabilityRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorUnavailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.DoctorUnavailability;
import com.patientapp.doctorservice.modules.doctor.mapper.DoctorUnavailabilityMapper;
import com.patientapp.doctorservice.modules.doctor.repository.DoctorUnavailabilityRepository;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorService;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorUnavailabilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorUnavailabilityServiceImpl implements DoctorUnavailabilityService {
    private final DoctorUnavailabilityRepository repository;
    private final DoctorUnavailabilityMapper mapper;
    private final DoctorService doctorService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DoctorUnavailabilityResponseDTO create(DoctorUnavailabilityRequestDTO request) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(request.doctorId());

        // Validation: start must be before end
        if(request.startTime().isAfter(request.endTime())) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        }

        // Optional: check for conflicts with other unavailability periods
        boolean conflict = !repository.findByDoctorAndStartTimeBetween(
                doctor,
                request.startTime(),
                request.endTime()
        ).isEmpty();

        if(conflict) {
            throw new ScheduleConflictException("Ya existe una ausencia en este rango de tiempo");
        }

        DoctorUnavailability entity = mapper.toEntity(request, doctor);
        return mapper.toResponseDTO(repository.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DoctorUnavailabilityResponseDTO> getByDoctorId(UUID doctorId) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(doctorId);
        return repository.findByDoctor(doctor)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorUnavailability> getAllEntitiesByDoctorId(UUID doctorId) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(doctorId);
        return repository.findByDoctor(doctor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        DoctorUnavailability entity = repository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Unavailability not found"));
        repository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DoctorUnavailability> getAllByDoctorIdOrThrow(UUID doctorId) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(doctorId);
         return repository.findByDoctor(doctor);
    }
}
