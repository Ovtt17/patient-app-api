package com.patientapp.patientservice.modules.patient.mapper;

import com.patientapp.patientservice.modules.patient.dto.PatientPagedResponseDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientRequestDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientResponseDTO;
import com.patientapp.patientservice.modules.patient.entity.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
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
    @DisplayName("toEntity should map all fields and trim notes")
    void toEntity_mapsFields_andTrimsNotes() {
        UUID userId = UUID.randomUUID();
        LocalDate birthDate = LocalDate.of(1990, 5, 12);
        PatientRequestDTO request = new PatientRequestDTO(
                userId,
                180.5,
                175.0,
                birthDate,
                "   Some notes with spaces   "
        );

        Patient entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertNull(entity.getId(), "ID should not be set by mapper");
        assertEquals(userId, entity.getUserId());
        assertEquals(180.5, entity.getWeight());
        assertEquals(175.0, entity.getHeight());
        assertEquals(birthDate, entity.getBirthDate());
        assertEquals("Some notes with spaces", entity.getNotes(), "Notes should be trimmed");
    }

    @Test
    @DisplayName("toPatientResponse should map entity to DTO correctly")
    void toPatientResponse_mapsFields() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate birthDate = LocalDate.of(1985, 1, 20);

        Patient patient = Patient.builder()
                .id(id)
                .userId(userId)
                .weight(150.0)
                .height(170.0)
                .birthDate(birthDate)
                .notes("Some notes")
                .build();

        PatientResponseDTO dto = mapper.toPatientResponse(patient);

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(userId, dto.userId());
        assertEquals(150.0, dto.weight());
        assertEquals(170.0, dto.height());
        assertEquals(birthDate, dto.birthDate());
        assertEquals("Some notes", dto.notes());
    }

    @Test
    @DisplayName("toPatientPagedResponseDTO should map a populated page correctly")
    void toPatientPagedResponseDTO_mapsPage() {
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            patients.add(Patient.builder()
                    .id(UUID.randomUUID())
                    .userId(UUID.randomUUID())
                    .weight(120.0 + i)
                    .height(160.0 + i)
                    .birthDate(LocalDate.of(1990, 1, 1).plusDays(i))
                    .notes("Notes " + i)
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
            assertEquals(source.getWeight(), target.weight());
            assertEquals(source.getHeight(), target.height());
            assertEquals(source.getBirthDate(), target.birthDate());
            assertEquals(source.getNotes(), target.notes());
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
        @Test
        @DisplayName("toEntity should throw NullPointerException when notes is null (current implementation)")
        void toEntity_nullNotes_throwsNpe() {
            PatientRequestDTO request = new PatientRequestDTO(
                    UUID.randomUUID(),
                    100.0,
                    180.0,
                    LocalDate.of(2000, 1, 1),
                    null
            );
            assertThrows(NullPointerException.class, () -> mapper.toEntity(request), "Current code calls trim() on notes without null check");
        }
    }
}

