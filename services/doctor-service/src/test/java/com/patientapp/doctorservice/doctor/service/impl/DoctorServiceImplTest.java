package com.patientapp.doctorservice.doctor.service.impl;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.mapper.DoctorMapper;
import com.patientapp.doctorservice.modules.doctor.repository.DoctorRepository;
import com.patientapp.doctorservice.common.handler.exceptions.DoctorNotFoundException;
import com.patientapp.doctorservice.common.handler.exceptions.EmailAlreadyInUseException;
import com.patientapp.doctorservice.common.handler.exceptions.SpecialtyNotFoundException;
import com.patientapp.doctorservice.modules.doctor.service.impl.DoctorServiceImpl;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import com.patientapp.doctorservice.modules.specialty.repository.SpecialtyRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DoctorServiceImpl using Mockito and JUnit 5.
 * This class validates the business logic for creating and retrieving doctors.
 */
@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @InjectMocks
    private DoctorServiceImpl doctorService; // Service under test

    @Mock
    private DoctorRepository doctorRepository; // Mock repository for doctors

    @Mock
    private SpecialtyRepository specialtyRepository; // Mock repository for specialties

    @Mock
    private DoctorMapper doctorMapper; // Mock mapper to convert between DTOs and entities

    private UUID doctorId;
    private Doctor doctor;
    private DoctorRequestDTO request;
    private Specialty specialty;

    /**
     * Initializes common test data before each test case.
     */
    @BeforeEach
    void setUp() {
        doctorId = UUID.randomUUID();

        // Create a sample specialty for testing
        specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("Cardiology");
        specialty.setDescription("Heart-related specialty");

        // Create a request DTO for doctor creation
        request = new DoctorRequestDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "+1234567890",
                "MED123",
                "101",
                Set.of(1),
                UUID.randomUUID()
        );

        // Create a doctor entity for testing
        doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setMedicalLicense(request.medicalLicense());
        doctor.setOfficeNumber(request.officeNumber());
        doctor.setUserId(request.userId());
        doctor.setActive(true);
        doctor.setSpecialties(Set.of(specialty));
    }

    /**
     * Tests successful creation of a doctor.
     * Verifies that the doctor is saved and a temporary password is returned.
     */
    @Test
    @DisplayName("create: should successfully create a doctor")
    void create_success() {
        when(doctorRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(doctorRepository.findByUserId(any(UUID.class))).thenReturn(Optional.empty());
        when(specialtyRepository.findByIdIn(any())).thenReturn(List.of(specialty));
        when(doctorMapper.toEntity(eq(request), anyList())).thenReturn(doctor);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        UUID resultId = doctorService.create(request);

        assertNotNull(resultId);
        assertEquals(doctor.getId(), resultId);

        verify(doctorRepository).findByEmail(request.email());
        verify(doctorRepository).findByUserId(request.userId());
        verify(doctorRepository).save(any(Doctor.class));
    }

    /**
     * Tests that an exception is thrown if the email already exists.
     */
    @Test
    @DisplayName("create: should throw EmailAlreadyInUseException if email exists")
    void create_emailExists() {
        when(doctorRepository.findByEmail(request.email())).thenReturn(Optional.of(doctor));

        assertThrows(EmailAlreadyInUseException.class, () -> doctorService.create(request));
        verify(doctorRepository).findByEmail(request.email());
        verify(doctorRepository, never()).save(any());
    }

    /**
     * Tests that an exception is thrown if the specialties provided do not exist.
     */
    @Test
    @DisplayName("create: should throw SpecialtyNotFoundException if specialties are missing")
    void create_specialtyNotFound() {
        when(doctorRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(doctorRepository.findByUserId(request.userId())).thenReturn(Optional.empty());
        when(specialtyRepository.findByIdIn(any())).thenReturn(List.of()); // No specialties found

        assertThrows(SpecialtyNotFoundException.class, () -> doctorService.create(request));
        verify(doctorRepository, never()).save(any());
    }

    /**
     * Tests successful retrieval of a doctor by ID.
     */
    @Test
    @DisplayName("getById: should return doctor if exists")
    void getById_success() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDoctorResponse(doctor)).thenReturn(
                new DoctorResponseDTO("John", "Doe", doctor.getEmail(), doctor.getPhone(),
                        doctor.getMedicalLicense(), doctor.getOfficeNumber(), doctor.getUserId().toString(),
                        List.of("Cardiology"))
        );

        DoctorResponseDTO result = doctorService.getById(doctorId);

        assertNotNull(result);
        assertEquals("John", result.firstName());
        verify(doctorMapper).toDoctorResponse(doctor);
    }

    /**
     * Tests that an exception is thrown if the doctor is not found by ID.
     */
    @Test
    @DisplayName("getById: should throw DoctorNotFoundException if not found")
    void getById_notFound() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.getById(doctorId));
    }
}