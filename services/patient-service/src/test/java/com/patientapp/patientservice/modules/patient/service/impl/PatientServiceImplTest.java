package com.patientapp.patientservice.modules.patient.service.impl;

import com.patientapp.patientservice.common.handler.exceptions.PatientNotFoundException;
import com.patientapp.patientservice.modules.patient.dto.PatientPagedResponseDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientRequestDTO;
import com.patientapp.patientservice.modules.patient.dto.PatientResponseDTO;
import com.patientapp.patientservice.modules.patient.entity.Patient;
import com.patientapp.patientservice.modules.patient.enums.Gender;
import com.patientapp.patientservice.modules.patient.mapper.PatientMapper;
import com.patientapp.patientservice.modules.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository repository;

    @Mock
    private PatientMapper mapper;

    @InjectMocks
    private PatientServiceImpl service;

    private UUID patientId;
    private UUID userId;

    @BeforeEach
    void init() {
        patientId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    private Patient buildPatient() {
        return Patient.builder()
                .id(patientId)
                .userId(userId)
                .active(true)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .phone("12345678")
                .gender(Gender.HOMBRE)
                .build();
    }

    // create
    @Test
    @DisplayName("create: should persist patient with all fields from PatientRequestDTO and return generated id")
    void create_success() {
        ArgumentCaptor<Patient> captor = ArgumentCaptor.forClass(Patient.class);
        PatientRequestDTO request = new PatientRequestDTO(
                "John",
                "Doe",
                "john.doe@email.com",
                "12345678",
                Gender.HOMBRE,
                null,
                userId
        );
        when(mapper.toEntity(request)).thenAnswer(inv -> {
            PatientRequestDTO req = inv.getArgument(0);
            return Patient.builder()
                    .userId(req.userId())
                    .firstName(req.firstName())
                    .lastName(req.lastName())
                    .email(req.email())
                    .phone(req.phone())
                    .gender(req.gender())
                    .active(true)
                    .build();
        });
        when(repository.save(any(Patient.class))).thenAnswer(inv -> {
            Patient p = inv.getArgument(0);
            p.setId(patientId);
            return p;
        });

        UUID result = service.create(request);

        verify(repository).save(captor.capture());
        Patient saved = captor.getValue();
        assertEquals(userId, saved.getUserId());
        assertEquals("John", saved.getFirstName());
        assertEquals("Doe", saved.getLastName());
        assertEquals("john.doe@email.com", saved.getEmail());
        assertEquals("12345678", saved.getPhone());
        assertEquals(Gender.HOMBRE, saved.getGender());
        assertTrue(saved.isActive());
        assertEquals(patientId, result);
    }

    // getAllActive empty
    @Test
    @DisplayName("getAllActive: returns null when repository returns empty page")
    void getAllActive_emptyReturnsNull() {
        when(repository.findAllByActiveTrue(any(Pageable.class)))
                .thenReturn(Page.empty());

        PatientPagedResponseDTO dto = service.getAllActive(0, 10, "id", "ASC", null, null, null);
        assertNull(dto);
    }

    // getAllActive non-empty ASC
    @Test
    @DisplayName("getAllActive: maps non-empty page ascending")
    void getAllActive_nonEmptyAsc() {
        Patient p = buildPatient();
        Page<Patient> page = new PageImpl<>(List.of(p), PageRequest.of(0, 5, Sort.by("userId").ascending()), 1);
        when(repository.findAllByActiveTrue(any(Pageable.class))).thenReturn(page);

        PatientResponseDTO responseDTO = PatientResponseDTO.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .gender(p.getGender())
                .build();
        PatientPagedResponseDTO pagedDTO = PatientPagedResponseDTO.builder()
                .content(List.of(responseDTO))
                .page(0)
                .totalPages(1)
                .totalElements(1)
                .build();

        when(mapper.toPatientPagedResponseDTO(page)).thenReturn(pagedDTO);

        PatientPagedResponseDTO result = service.getAllActive(0, 5, "userId", "ASC", null, null, null);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(0, result.page());
        assertEquals(1, result.totalPages());
        verify(mapper).toPatientPagedResponseDTO(page);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAllByActiveTrue(pageableCaptor.capture());
        Pageable used = pageableCaptor.getValue();
        assertEquals(0, used.getPageNumber());
        assertEquals(5, used.getPageSize());
        assertEquals(Sort.Direction.ASC, Objects.requireNonNull(used.getSort().getOrderFor("userId")).getDirection());
    }

    // getAllActive non-empty DESC
    @Test
    @DisplayName("getAllActive: uses descending sort when requested")
    void getAllActive_nonEmptyDesc() {
        Patient p = buildPatient();
        Page<Patient> page = new PageImpl<>(List.of(p), PageRequest.of(1, 3, Sort.by("birthDate").descending()), 4);
        when(repository.findAllByActiveTrue(any(Pageable.class))).thenReturn(page);

        PatientPagedResponseDTO pagedDTO = PatientPagedResponseDTO.builder()
                .content(List.of())
                .page(1)
                .totalPages(2)
                .totalElements(4)
                .build();
        when(mapper.toPatientPagedResponseDTO(page)).thenReturn(pagedDTO);

        PatientPagedResponseDTO result = service.getAllActive(1, 3, "birthDate", "DESC", null, null, null);
        assertNotNull(result);
        assertEquals(1, result.page());
        assertEquals(2, result.totalPages());
        assertEquals(4, result.totalElements());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAllByActiveTrue(pageableCaptor.capture());
        Pageable used = pageableCaptor.getValue();
        assertEquals(Sort.Direction.DESC, Objects.requireNonNull(used.getSort().getOrderFor("birthDate")).getDirection());
    }

    // getById
    @Test
    @DisplayName("getById: returns mapped DTO")
    void getById_success() {
        Patient p = buildPatient();
        when(repository.findByIdAndActiveTrue(patientId)).thenReturn(Optional.of(p));
        PatientResponseDTO dto = PatientResponseDTO.builder().id(p.getId()).userId(p.getUserId()).build();
        when(mapper.toPatientResponse(p)).thenReturn(dto);

        PatientResponseDTO result = service.getById(patientId);
        assertEquals(p.getId(), result.id());
        verify(mapper).toPatientResponse(p);
    }

    // getEntityByIdOrThrow found
    @Test
    @DisplayName("getEntityByIdOrThrow: returns entity when found")
    void getEntityByIdOrThrow_found() {
        Patient p = buildPatient();
        when(repository.findByIdAndActiveTrue(patientId)).thenReturn(Optional.of(p));
        Patient result = service.getEntityByIdOrThrow(patientId);
        assertEquals(p, result);
    }

    // getEntityByIdOrThrow not found
    @Test
    @DisplayName("getEntityByIdOrThrow: throws when not found")
    void getEntityByIdOrThrow_notFound() {
        when(repository.findByIdAndActiveTrue(patientId)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> service.getEntityByIdOrThrow(patientId));
    }

    // deactivate
    @Test
    @DisplayName("deactivate: sets active false and saves")
    void deactivate_success() {
        Patient existing = buildPatient();
        when(repository.findByIdAndActiveTrue(patientId)).thenReturn(Optional.of(existing));

        service.deactivate(patientId);

        assertFalse(existing.isActive());
        verify(repository).save(existing);
    }
}
