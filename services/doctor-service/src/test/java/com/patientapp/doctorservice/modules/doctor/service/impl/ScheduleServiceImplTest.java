package com.patientapp.doctorservice.modules.doctor.service.impl;

import com.patientapp.doctorservice.common.handler.exceptions.ScheduleConflictException;
import com.patientapp.doctorservice.common.handler.exceptions.ScheduleNotFoundException;
import com.patientapp.doctorservice.modules.doctor.dto.ScheduleRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.ScheduleResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.Schedule;
import com.patientapp.doctorservice.modules.doctor.mapper.ScheduleMapper;
import com.patientapp.doctorservice.modules.doctor.repository.ScheduleRepository;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ScheduleServiceImplTest {
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private DoctorService doctorService;
    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private Doctor doctor;
    private Schedule schedule;
    private ScheduleRequestDTO requestDTO;
    private ScheduleResponseDTO responseDTO;
    private AutoCloseable mocks;

    private LocalTime startTime;
    private LocalTime endTime;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        doctor.setActive(true);
        doctor.setUserId(UUID.randomUUID());
        doctor.setZoneId("America/Lima");

        startTime = LocalTime.of(9, 0);
        endTime = LocalTime.of(12, 0);

        schedule = new Schedule();
        schedule.setId(1);
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);

        requestDTO = new ScheduleRequestDTO(
                DayOfWeek.MONDAY,
                startTime,
                endTime,
                doctor.getId()
        );

        responseDTO = ScheduleResponseDTO.builder()
                .id(1)
                .doctorId(doctor.getId())
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    void createSchedule_success() {
        when(scheduleRepository.existsConflict(any(), any(), any(), any(), isNull())).thenReturn(false);
        when(doctorService.getEntityByIdOrThrow(any())).thenReturn(doctor);
        when(scheduleMapper.toEntity(any(), any())).thenReturn(schedule);
        when(scheduleRepository.save(any())).thenReturn(schedule);
        when(scheduleMapper.toResponseDTO(any())).thenReturn(responseDTO);

        ScheduleResponseDTO result = scheduleService.create(requestDTO);
        assertEquals(responseDTO, result);
    }

    @Test
    void createSchedule_conflict_throwsException() {
        when(scheduleRepository.existsConflict(any(), any(), any(), any(), isNull())).thenReturn(true);
        assertThrows(ScheduleConflictException.class, () -> scheduleService.create(requestDTO));
    }

    @Test
    void createSchedule_invalidTimes_throwsException() {
        ScheduleRequestDTO invalidRequest = new ScheduleRequestDTO(
                DayOfWeek.MONDAY,
                endTime,
                startTime,
                doctor.getId()
        );
        assertThrows(IllegalArgumentException.class, () -> scheduleService.create(invalidRequest));
    }

    @Test
    void getById_success() {
        when(scheduleRepository.findById(anyInt())).thenReturn(Optional.of(schedule));
        when(scheduleMapper.toResponseDTO(any())).thenReturn(responseDTO);
        ScheduleResponseDTO result = scheduleService.getById(1);
        assertEquals(responseDTO, result);
    }

    @Test
    void getById_notFound_throwsException() {
        when(scheduleRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ScheduleNotFoundException.class, () -> scheduleService.getById(99));
    }

    @Test
    void getEntityByIdOrThrow_success() {
        when(scheduleRepository.findById(anyInt())).thenReturn(Optional.of(schedule));
        Schedule result = scheduleService.getEntityByIdOrThrow(1);
        assertEquals(schedule, result);
    }

    @Test
    void getEntityByIdOrThrow_notFound_throwsException() {
        when(scheduleRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ScheduleNotFoundException.class, () -> scheduleService.getEntityByIdOrThrow(99));
    }

    @Test
    void getByDoctorId_success() {
        when(doctorService.getEntityByIdOrThrow(any())).thenReturn(doctor);
        when(scheduleRepository.findByDoctor(any())).thenReturn(List.of(schedule));
        when(scheduleMapper.toResponseDTO(any())).thenReturn(responseDTO);
        List<ScheduleResponseDTO> result = scheduleService.getByDoctorId(doctor.getId());
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
    }

    @Test
    void getByDoctorIdAndDay_success() {
        when(doctorService.getEntityByIdOrThrow(any())).thenReturn(doctor);
        when(scheduleRepository.findByDoctorAndDayOfWeek(any(), any())).thenReturn(List.of(schedule));
        when(scheduleMapper.toResponseDTO(any())).thenReturn(responseDTO);
        List<ScheduleResponseDTO> result = scheduleService.getByDoctorIdAndDay(doctor.getId(), DayOfWeek.MONDAY);
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
    }

    @Test
    void updateSchedule_success() {
        when(scheduleRepository.findById(anyInt())).thenReturn(Optional.of(schedule));
        when(scheduleRepository.existsConflict(any(), any(), any(), any(), anyInt())).thenReturn(false);
        doNothing().when(scheduleMapper).updateEntity(any(), any());
        when(scheduleRepository.save(any())).thenReturn(schedule);
        when(scheduleMapper.toResponseDTO(any())).thenReturn(responseDTO);
        ScheduleResponseDTO result = scheduleService.update(1, requestDTO);
        assertEquals(responseDTO, result);
    }

    @Test
    void updateSchedule_conflict_throwsException() {
        when(scheduleRepository.findById(anyInt())).thenReturn(Optional.of(schedule));
        when(scheduleRepository.existsConflict(any(), any(), any(), any(), anyInt())).thenReturn(true);
        assertThrows(ScheduleConflictException.class, () -> scheduleService.update(1, requestDTO));
    }

    @Test
    void updateSchedule_invalidTimes_throwsException() {
        when(scheduleRepository.findById(anyInt())).thenReturn(Optional.of(schedule));
        ScheduleRequestDTO invalidRequest = new ScheduleRequestDTO(
                DayOfWeek.MONDAY,
                endTime,
                startTime,
                doctor.getId()
        );
        assertThrows(IllegalArgumentException.class, () -> scheduleService.update(1, invalidRequest));
    }

    @Test
    void deleteSchedule_success() {
        when(scheduleRepository.findById(anyInt())).thenReturn(Optional.of(schedule));
        doNothing().when(scheduleRepository).delete(any());
        assertDoesNotThrow(() -> scheduleService.delete(1));
        verify(scheduleRepository, times(1)).delete(schedule);
    }

    @Test
    void deleteSchedule_notFound_throwsException() {
        when(scheduleRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ScheduleNotFoundException.class, () -> scheduleService.delete(99));
    }

    @Test
    void deleteAllByDoctorId_success() {
        when(doctorService.getEntityByIdOrThrow(any())).thenReturn(doctor);
        when(scheduleRepository.findByDoctor(any())).thenReturn(List.of(schedule));
        doNothing().when(scheduleRepository).deleteAll(any());
        assertDoesNotThrow(() -> scheduleService.deleteAllByDoctorId(doctor.getId()));
        verify(scheduleRepository, times(1)).deleteAll(List.of(schedule));
    }
}