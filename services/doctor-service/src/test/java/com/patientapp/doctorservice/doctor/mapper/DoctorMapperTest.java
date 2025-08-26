package com.patientapp.doctorservice.doctor.mapper;

import com.patientapp.doctorservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.doctor.entity.Doctor;
import com.patientapp.doctorservice.specialty.entity.Specialty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DoctorMapperTest {

    private DoctorMapper doctorMapper;

    @BeforeEach
    void setUp() {
        doctorMapper = new DoctorMapper();
    }

    @Test
    void should_map_doctor_request_to_entity() {
        // Arrange
        UUID userId = UUID.randomUUID();
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setEmail("juan@example.com");
        dto.setPhone("555-1234");
        dto.setMedicalLicense("LIC12345");
        dto.setOfficeNumber("101");
        dto.setUserId(userId);
        dto.setSpecialtyIds(Set.of(1, 2));

        Specialty specialty1 = new Specialty();
        specialty1.setId(1);
        specialty1.setName("Cardiología");

        Specialty specialty2 = new Specialty();
        specialty2.setId(2);
        specialty2.setName("Neurología");

        List<Specialty> specialties = List.of(specialty1, specialty2);

        // Act
        Doctor doctor = doctorMapper.toEntity(dto, specialties);

        // Assert
        assertNotNull(doctor);
        assertEquals(dto.getFirstName(), doctor.getFirstName());
        assertEquals(dto.getLastName(), doctor.getLastName());
        assertEquals(dto.getEmail(), doctor.getEmail());
        assertEquals(dto.getPhone(), doctor.getPhone());
        assertEquals(dto.getMedicalLicense(), doctor.getMedicalLicense());
        assertEquals(dto.getOfficeNumber(), doctor.getOfficeNumber());
        assertEquals(userId, doctor.getUserId());
        assertTrue(doctor.isActive());
        assertEquals(specialties.size(), doctor.getSpecialties().size());
        assertTrue(doctor.getSpecialties().contains(specialty1));
        assertTrue(doctor.getSpecialties().contains(specialty2));
    }
}