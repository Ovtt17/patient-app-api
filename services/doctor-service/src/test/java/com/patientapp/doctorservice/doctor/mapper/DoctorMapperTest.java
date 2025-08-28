package com.patientapp.doctorservice.doctor.mapper;

import com.patientapp.doctorservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.doctor.dto.DoctorResponseDTO;
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
        DoctorRequestDTO dto = new DoctorRequestDTO(
                "Juan",
                "Pérez",
                "juan@example.com",
                "87227697",
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
        assertEquals(dto.firstName(), doctor.getFirstName());
        assertEquals(dto.lastName(), doctor.getLastName());
        assertEquals(dto.email(), doctor.getEmail());
        assertEquals(dto.phone(), doctor.getPhone());
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
                "Ana",
                "García",
                "ana@example.com",
                "12345678",
                "LIC54321",
                "202",
                Set.of(),
                userId
        );
        List<Specialty> specialties = List.of();
        Doctor doctor = doctorMapper.toEntity(dto, specialties);
        assertNotNull(doctor);
        assertEquals(0, doctor.getSpecialties().size());
    }

    @Test
    void should_map_doctor_request_to_entity_with_null_specialties() {
        UUID userId = UUID.randomUUID();
        DoctorRequestDTO dto = new DoctorRequestDTO(
                "Luis",
                "Martínez",
                "luis@example.com",
                "99999999",
                "LIC99999",
                "303",
                Set.of(),
                userId
        );
        Doctor doctor = doctorMapper.toEntity(dto, null);
        assertNotNull(doctor);
        assertNotNull(doctor.getSpecialties());
        assertEquals(0, doctor.getSpecialties().size());
    }

    @Test
    void should_map_doctor_to_doctor_response() {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("Pediatría");
        Doctor doctor = new Doctor();
        doctor.setFirstName("Maria");
        doctor.setLastName("Lopez");
        doctor.setEmail("maria@example.com");
        doctor.setPhone("55555555");
        doctor.setMedicalLicense("LIC00001");
        doctor.setOfficeNumber("404");
        UUID userId = UUID.randomUUID();
        doctor.setUserId(userId);
        doctor.setSpecialties(Set.of(specialty));
        DoctorResponseDTO response = doctorMapper.toDoctorResponse(doctor);
        assertEquals("Maria", response.firstName());
        assertEquals("Lopez", response.lastName());
        assertEquals("maria@example.com", response.email());
        assertEquals("55555555", response.phone());
        assertEquals("LIC00001", response.medicalLicense());
        assertEquals("404", response.officeNumber());
        assertEquals(userId.toString(), response.userId());
        assertEquals(List.of("Pediatría"), response.specialties());
    }

    @Test
    void should_map_doctor_to_doctor_response_with_null_specialties() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Carlos");
        doctor.setLastName("Ruiz");
        doctor.setEmail("carlos@example.com");
        doctor.setPhone("11112222");
        doctor.setMedicalLicense("LIC22222");
        doctor.setOfficeNumber("505");
        doctor.setUserId(UUID.randomUUID());
        doctor.setSpecialties(null);
        DoctorResponseDTO response = doctorMapper.toDoctorResponse(doctor);
        assertNotNull(response);
        assertTrue(response.specialties().isEmpty());
    }
}