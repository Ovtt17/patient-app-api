package com.patientapp.doctorservice.modules.doctor.mapper;

import com.patientapp.doctorservice.modules.doctor.dto.ScheduleRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.ScheduleResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleMapperTest {
    private ScheduleMapper mapper;
    private Doctor doctor;
    private ScheduleRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mapper = new ScheduleMapper();
        doctor = Doctor.builder()
                .id(UUID.randomUUID())
                .active(true)
                .medicalLicense("ML123")
                .officeNumber("101")
                .userId(UUID.randomUUID())
                .zoneId("America/Mexico_City")
                .build();

        requestDTO = new ScheduleRequestDTO(
                DayOfWeek.MONDAY,
                Instant.parse("2025-09-15T15:00:00Z"),
                Instant.parse("2025-09-15T23:00:00Z"),
                doctor.getId()
        );
    }

    @Test
    void testToEntity() {
        Schedule schedule = mapper.toEntity(requestDTO, doctor);

        assertEquals(doctor, schedule.getDoctor());
        assertEquals(requestDTO.startTime(), schedule.getStartTime());
        assertEquals(requestDTO.endTime(), schedule.getEndTime());
    }

    @Test
    void testToResponseDTO() {
        Schedule schedule = Schedule.builder()
                .id(1)
                .doctor(doctor)
                .startTime(Instant.parse("2025-09-16T14:00:00Z"))
                .endTime(Instant.parse("2025-09-16T22:00:00Z"))
                .build();

        ScheduleResponseDTO responseDTO = mapper.toResponseDTO(schedule);

        assertEquals(schedule.getId(), responseDTO.id());
        assertEquals(schedule.getDoctor().getId(), responseDTO.doctorId());
        assertEquals(schedule.getStartTime(), responseDTO.startTime());
        assertEquals(schedule.getEndTime(), responseDTO.endTime());
        assertEquals(schedule.getDoctor().getZoneId(), responseDTO.zoneId());
    }

    @Test
    void testUpdateEntity() {
        Schedule schedule = Schedule.builder()
                .id(2)
                .doctor(doctor)
                .startTime(Instant.parse("2025-09-15T10:00:00Z"))
                .endTime(Instant.parse("2025-09-15T18:00:00Z"))
                .build();

        mapper.updateEntity(schedule, requestDTO);

        assertEquals(requestDTO.startTime(), schedule.getStartTime());
        assertEquals(requestDTO.endTime(), schedule.getEndTime());
    }
}