package com.patientapp.doctorservice.modules.specialty.mapper;

import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyRequestDTO;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyResponseDTO;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialtyMapperTest {

    private final SpecialtyMapper mapper = new SpecialtyMapper();

    @Test
    void toEntity_withValidFields_trimsAndMaps() {
        SpecialtyRequestDTO request = new SpecialtyRequestDTO("  Cardiología  ", "  Corazón y sistema circulatorio  ");

        Specialty entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Cardiología", entity.getName());
        assertEquals("Corazón y sistema circulatorio", entity.getDescription());
    }

    @Test
    void toEntity_withBlankFields_setsNulls() {
        SpecialtyRequestDTO request = new SpecialtyRequestDTO("   ", "   ");

        Specialty entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertNull(entity.getName());
        assertNull(entity.getDescription());
    }

    @Test
    void toResponseDTO_withValidEntity_mapsFields() {
        Specialty entity = Specialty.builder()
                .id(1)
                .name("Dermatología")
                .description("Piel")
                .build();

        SpecialtyResponseDTO dto = mapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(1, dto.id());
        assertEquals("Dermatología", dto.name());
        assertEquals("Piel", dto.description());
    }

    @Test
    void toResponseDTO_withBlankFields_setsNulls() {
        Specialty entity = Specialty.builder()
                .id(null)
                .name("   ")
                .description("")
                .build();

        SpecialtyResponseDTO dto = mapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertNull(dto.id());
        assertNull(dto.name());
        assertNull(dto.description());
    }

    @Test
    void update_withValidFields_updatesAndTrims() {
        Specialty existing = Specialty.builder()
                .id(10)
                .name("Antiguo")
                .description("Desc antigua")
                .build();

        SpecialtyRequestDTO request = new SpecialtyRequestDTO("  Neurología  ", "  Sistema nervioso  ");

        Specialty updated = mapper.update(request, existing);

        assertSame(existing, updated);
        assertEquals(10, updated.getId());
        assertEquals("Neurología", updated.getName());
        assertEquals("Sistema nervioso", updated.getDescription());
    }

    @Test
    void update_withBlankFields_setsNulls() {
        Specialty existing = Specialty.builder()
                .id(5)
                .name("Nombre")
                .description("Descr")
                .build();

        SpecialtyRequestDTO request = new SpecialtyRequestDTO("   ", null);

        Specialty updated = mapper.update(request, existing);

        assertNull(updated.getName());
        assertNull(updated.getDescription());
    }
}
