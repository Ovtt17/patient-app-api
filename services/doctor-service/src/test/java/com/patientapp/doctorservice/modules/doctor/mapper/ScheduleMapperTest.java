package com.patientapp.doctorservice.modules.doctor.mapper;

import com.patientapp.doctorservice.modules.doctor.dto.ScheduleRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.ScheduleResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                LocalTime.of(9,0),
                LocalTime.of(17,0),
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
                .dayOfWeek(DayOfWeek.TUESDAY)
                .startTime(LocalTime.of(8,0))
                .endTime(LocalTime.of(16,0))
                .build();

        ScheduleResponseDTO responseDTO = mapper.toResponseDTO(schedule);
        assertEquals(schedule.getId(), responseDTO.id());
        assertEquals(schedule.getDoctor().getId(), responseDTO.doctorId());
        assertEquals(schedule.getDayOfWeek(), responseDTO.dayOfWeek());
        assertEquals(schedule.getStartTime(), responseDTO.startTime());
        assertEquals(schedule.getEndTime(), responseDTO.endTime());
    }

    @Test
    void testUpdateEntity() {
        Schedule schedule = Schedule.builder()
                .id(2)
                .doctor(doctor)
                .dayOfWeek(DayOfWeek.WEDNESDAY)
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(18,0))
                .build();

        mapper.updateEntity(schedule, requestDTO);
        assertEquals(requestDTO.dayOfWeek(), schedule.getDayOfWeek());
        assertEquals(requestDTO.startTime(), schedule.getStartTime());
        assertEquals(requestDTO.endTime(), schedule.getEndTime());
    }
}