package com.patientapp.doctorservice.doctor.mapper;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.mapper.DoctorMapper;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
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
        DoctorRequestDTO dto = new DoctorRequestDTO(
                "LIC12345",
                "101",
                Set.of(1, 2),
                userId
        );

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
        assertEquals(dto.medicalLicense(), doctor.getMedicalLicense());
        assertEquals(dto.officeNumber(), doctor.getOfficeNumber());
        assertEquals(userId, doctor.getUserId());
        assertTrue(doctor.isActive());
        assertEquals(2, doctor.getSpecialties().size());
    }

    @Test
    void should_map_doctor_request_to_entity_with_empty_specialties() {
        UUID userId = UUID.randomUUID();
        DoctorRequestDTO dto = new DoctorRequestDTO(
                "LIC54321",
                "202",
                Set.of(),
                userId
        );
        List<Specialty> specialties = List.of();
        Doctor doctor = doctorMapper.toEntity(dto, specialties);

        assertNotNull(doctor);
        assertEquals(0, doctor.getSpecialties().size());
        assertEquals(dto.medicalLicense(), doctor.getMedicalLicense());
        assertEquals(dto.officeNumber(), doctor.getOfficeNumber());
    }

    @Test
    void should_map_doctor_request_to_entity_with_null_specialties() {
        UUID userId = UUID.randomUUID();
        DoctorRequestDTO dto = new DoctorRequestDTO(
                "LIC99999",
                "303",
                Set.of(),
                userId
        );
        Doctor doctor = doctorMapper.toEntity(dto, null);

        assertNotNull(doctor);
        assertNotNull(doctor.getSpecialties());
        assertEquals(0, doctor.getSpecialties().size());
        assertEquals(dto.medicalLicense(), doctor.getMedicalLicense());
        assertEquals(dto.officeNumber(), doctor.getOfficeNumber());
    }

    @Test
    void should_map_doctor_to_doctor_response() {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("Pediatría");

        Doctor doctor = Doctor.builder()
                .medicalLicense("LIC00001")
                .officeNumber("404")
                .userId(UUID.randomUUID())
                .specialties(Set.of(specialty))
                .active(true)
                .build();

        DoctorResponseDTO response = doctorMapper.toDoctorResponse(doctor);

        assertNotNull(response);
        assertEquals("LIC00001", response.medicalLicense());
        assertEquals("404", response.officeNumber());
        assertEquals(doctor.getUserId().toString(), response.userId());
        assertEquals(List.of("Pediatría"), response.specialties());
    }

    @Test
    void should_map_doctor_to_doctor_response_with_null_specialties() {
        Doctor doctor = Doctor.builder()
                .medicalLicense("LIC22222")
                .officeNumber("505")
                .userId(UUID.randomUUID())
                .specialties(null)
                .active(true)
                .build();

        DoctorResponseDTO response = doctorMapper.toDoctorResponse(doctor);

        assertNotNull(response);
        assertEquals("LIC22222", response.medicalLicense());
        assertEquals("505", response.officeNumber());
        assertTrue(response.specialties().isEmpty());
    }
}