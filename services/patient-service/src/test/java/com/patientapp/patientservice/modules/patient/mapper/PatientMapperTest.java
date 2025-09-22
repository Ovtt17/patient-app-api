package com.patientapp.patientservice.modules.patient.mapper;

import com.patientapp.patientservice.modules.patient.dto.PatientPagedResponseDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientRequestDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientResponseDTO;
import com.patientapp.patientservice.modules.patient.entity.Patient;
import com.patientapp.patientservice.modules.patient.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PatientMapperTest {

    private PatientMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PatientMapper();
    }

    @Test
    @DisplayName("toEntity should map all fields")
    void toEntity_mapsFields() {
        UUID userId = UUID.randomUUID();
        PatientRequestDTO request = new PatientRequestDTO(
                "John",
                "Doe",
                "john.doe@email.com",
                "12345678",
                Gender.HOMBRE,
                null,
                userId
        );

        Patient entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertNull(entity.getId(), "ID should not be set by mapper");
        assertEquals(userId, entity.getUserId());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("john.doe@email.com", entity.getEmail());
        assertEquals("12345678", entity.getPhone());
        assertEquals(Gender.HOMBRE, entity.getGender());
    }

    @Test
    @DisplayName("toPatientResponse should map entity to DTO correctly")
    void toPatientResponse_mapsFields() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Patient patient = Patient.builder()
                .id(id)
                .userId(userId)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@email.com")
                .phone("87654321")
                .gender(Gender.MUJER)
                .build();

        PatientResponseDTO dto = mapper.toPatientResponse(patient);

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(userId, dto.userId());
        assertEquals("Jane", dto.firstName());
        assertEquals("Smith", dto.lastName());
        assertEquals("jane.smith@email.com", dto.email());
        assertEquals("87654321", dto.phone());
        assertEquals(Gender.MUJER, dto.gender());
    }

    @Test
    @DisplayName("toPatientPagedResponseDTO should map a populated page correctly")
    void toPatientPagedResponseDTO_mapsPage() {
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            patients.add(Patient.builder()
                    .id(UUID.randomUUID())
                    .userId(UUID.randomUUID())
                    .firstName("Name" + i)
                    .lastName("Last" + i)
                    .email("email" + i + "@test.com")
                    .phone("1000000" + i)
                    .gender(i % 2 == 0 ? Gender.HOMBRE : Gender.MUJER)
                    .build());
        }
        PageRequest pageRequest = PageRequest.of(1, 2); // page index 1
        Page<Patient> page = new PageImpl<>(patients, pageRequest, 5); // total elements 5 -> total pages 3

        PatientPagedResponseDTO dto = mapper.toPatientPagedResponseDTO(page);

        assertNotNull(dto);
        assertEquals(2, dto.patients().size());
        assertEquals(1, dto.page());
        assertEquals(3, dto.totalPages());
        assertEquals(5, dto.totalElements());

        // Verify individual patient mapping
        for (int i = 0; i < patients.size(); i++) {
            Patient source = patients.get(i);
            PatientResponseDTO target = dto.patients().get(i);
            assertEquals(source.getId(), target.id());
            assertEquals(source.getUserId(), target.userId());
            assertEquals(source.getFirstName(), target.firstName());
            assertEquals(source.getLastName(), target.lastName());
            assertEquals(source.getEmail(), target.email());
            assertEquals(source.getPhone(), target.phone());
            assertEquals(source.getGender(), target.gender());
        }
    }

    @Test
    @DisplayName("toPatientPagedResponseDTO should handle empty page")
    void toPatientPagedResponseDTO_emptyPage() {
        Page<Patient> emptyPage = Page.empty(PageRequest.of(0, 10));

        PatientPagedResponseDTO dto = mapper.toPatientPagedResponseDTO(emptyPage);

        assertNotNull(dto);
        assertNotNull(dto.patients());
        assertTrue(dto.patients().isEmpty());
        assertEquals(0, dto.page());
        assertEquals(0, dto.totalPages());
        assertEquals(0, dto.totalElements());
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {
        // Remove edge case for notes/null as notes is no longer present
    }
}
