package com.patientapp.doctorservice.modules.specialty.service.impl;

import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyRequestDTO;
import com.patientapp.doctorservice.modules.specialty.dto.SpecialtyResponseDTO;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import com.patientapp.doctorservice.modules.specialty.mapper.SpecialtyMapper;
import com.patientapp.doctorservice.modules.specialty.repository.SpecialtyRepository;
import com.patientapp.doctorservice.modules.specialty.service.SpecialtyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyServiceImplTest {

    @InjectMocks
    private SpecialtyServiceImpl service;

    @Mock
    private SpecialtyRepository repository;

    @Mock
    private SpecialtyMapper mapper;

    @Test
    void should_create_specialty() {
        // Arrange
        SpecialtyRequestDTO request = new SpecialtyRequestDTO("  Cardiología  ", "  Corazón  ");
        Specialty entityToSave = Specialty.builder()
                .id(null)
                .name("Cardiología")
                .description("Corazón")
                .build();
        Specialty savedEntity = Specialty.builder()
                .id(1)
                .name("Cardiología")
                .description("Corazón")
                .build();
        SpecialtyResponseDTO responseDTO = new SpecialtyResponseDTO(1, "Cardiología", "Corazón");

        // Mock del mapper
        when(mapper.toEntity(request)).thenReturn(entityToSave);
        when(mapper.toResponseDTO(savedEntity)).thenReturn(responseDTO);

        // Mock del repositorio
        when(repository.save(entityToSave)).thenReturn(savedEntity);

        // Act
        SpecialtyResponseDTO response = service.create(request);

        // Assert: repository save llamado correctamente
        ArgumentCaptor<Specialty> captor = ArgumentCaptor.forClass(Specialty.class);
        verify(repository, times(1)).save(captor.capture());
        Specialty captured = captor.getValue();
        assertEquals("Cardiología", captured.getName());
        assertEquals("Corazón", captured.getDescription());

        // Assert: response devuelto correctamente
        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("Cardiología", response.name());
        assertEquals("Corazón", response.description());
    }

    @Test
    void should_update_specialty() {
        // Arrange
        Integer specialtyId = 1;
        SpecialtyRequestDTO request = new SpecialtyRequestDTO("  Neurología  ", "  Sistema nervioso  ");

        Specialty existingSpecialty = Specialty.builder()
                .id(specialtyId)
                .name("Antiguo")
                .description("Antigua descripción")
                .build();

        Specialty updatedSpecialty = Specialty.builder()
                .id(specialtyId)
                .name("Neurología")
                .description("Sistema nervioso")
                .build();

        Specialty savedSpecialty = Specialty.builder()
                .id(specialtyId)
                .name("Neurología")
                .description("Sistema nervioso")
                .build();

        SpecialtyResponseDTO responseDTO = new SpecialtyResponseDTO(specialtyId, "Neurología", "Sistema nervioso");

        // spy service to mock findByIdOrThrow and save methods
        SpecialtyServiceImpl spyService = spy(service);
        doReturn(existingSpecialty).when(spyService).findByIdOrThrow(specialtyId);
        doReturn(savedSpecialty).when(spyService).save(updatedSpecialty);

        // Mock mapper
        when(mapper.update(request, existingSpecialty)).thenReturn(updatedSpecialty);
        when(mapper.toResponseDTO(savedSpecialty)).thenReturn(responseDTO);

        // Act
        SpecialtyResponseDTO response = spyService.update(specialtyId, request);

        // Assert
        assertNotNull(response);
        assertEquals(specialtyId, response.id());
        assertEquals("Neurología", response.name());
        assertEquals("Sistema nervioso", response.description());

        // Verify interactions
        verify(mapper, times(1)).update(request, existingSpecialty);
        verify(spyService, times(1)).save(updatedSpecialty);
    }

    @Test
    void should_delete_specialty() {
        // Arrange
        Integer specialtyId = 1;
        Specialty specialty = Specialty.builder()
                .id(specialtyId)
                .name("Cardiología")
                .description("Corazón y sistema circulatorio")
                .build();

        // Spy service to mock findByIdOrThrow
        SpecialtyServiceImpl spyService = spy(service);
        doReturn(specialty).when(spyService).findByIdOrThrow(specialtyId);

        // Act
        String result = spyService.delete(specialtyId);

        // Assert
        assertEquals("Se ha eliminado la especialidad: Cardiología", result);

        // Verify interactions
        verify(repository, times(1)).delete(specialty);
    }
}