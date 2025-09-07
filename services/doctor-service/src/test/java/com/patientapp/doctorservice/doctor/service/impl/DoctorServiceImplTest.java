package com.patientapp.doctorservice.doctor.service.impl;

import com.patientapp.doctorservice.common.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.common.handler.exceptions.SpecialtyNotFoundException;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.mapper.DoctorMapper;
import com.patientapp.doctorservice.modules.doctor.repository.DoctorRepository;
import com.patientapp.doctorservice.modules.doctor.service.impl.DoctorServiceImpl;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import com.patientapp.doctorservice.modules.specialty.service.SpecialtyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DoctorServiceImpl using Mockito and JUnit 5.
 * This class validates the business logic for creating and retrieving doctors.
 */
@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private SpecialtyService specialtyService;

    @Mock
    private DoctorMapper doctorMapper;

    private UUID doctorId;
    private Doctor doctor;
    private DoctorRequestDTO request;
    private Specialty specialty;
    private UUID userId;

    @BeforeEach
    void setUp() {
        doctorId = UUID.randomUUID();
        userId = UUID.randomUUID();

        // Specialty de ejemplo
        specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("Cardiology");
        specialty.setDescription("Heart-related specialty");

        // Request para update
        request = new DoctorRequestDTO(
                "MED123",
                "101",
                Set.of(1),
                userId
        );

        // Doctor de prueba
        doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setMedicalLicense(request.medicalLicense());
        doctor.setOfficeNumber(request.officeNumber());
        doctor.setUserId(userId);
        doctor.setActive(true);
        doctor.setSpecialties(Set.of(specialty));
    }

    @Test
    @DisplayName("create: should successfully create a doctor")
    void create_success() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        UUID resultId = doctorService.create(userId);

        assertNotNull(resultId);
        assertEquals(doctor.getId(), resultId);
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    @DisplayName("update: should successfully update a doctor")
    void update_success() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(specialtyService.findByIdIn(anyList())).thenReturn(List.of(specialty));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(doctorMapper.toDoctorResponse(doctor)).thenReturn(
                new DoctorResponseDTO(
                        "John",
                        "Doe",
                        null,
                        null,
                        doctor.getMedicalLicense(),
                        doctor.getOfficeNumber(),
                        doctor.getUserId().toString(),
                        List.of("Cardiology")
                )
        );

        DoctorResponseDTO updated = doctorService.update(doctorId, request);

        assertNotNull(updated);
        assertEquals(request.medicalLicense(), updated.medicalLicense());
        verify(doctorRepository).findById(doctorId);
        verify(doctorRepository).save(doctor);
    }

    @Test
    @DisplayName("update: should throw SpecialtyNotFoundException if specialties missing")
    void update_specialtyNotFound() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(specialtyService.findByIdIn(anyList())).thenReturn(List.of()); // No specialties

        assertThrows(SpecialtyNotFoundException.class, () -> doctorService.update(doctorId, request));
        verify(doctorRepository, never()).save(any());
    }

    @Test
    @DisplayName("getById: should return doctor if exists")
    void getById_success() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDoctorResponse(doctor)).thenReturn(
                new DoctorResponseDTO(
                        "John",
                        "Doe",
                        null,
                        null,
                        doctor.getMedicalLicense(),
                        doctor.getOfficeNumber(),
                        doctor.getUserId().toString(),
                        List.of("Cardiology")
                )
        );

        DoctorResponseDTO result = doctorService.getById(doctorId);

        assertNotNull(result);
        assertEquals("John", result.firstName());
        verify(doctorMapper).toDoctorResponse(doctor);
    }

    @Test
    @DisplayName("getById: should throw DoctorNotFoundException if not found")
    void getById_notFound() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.getById(doctorId));
    }
}