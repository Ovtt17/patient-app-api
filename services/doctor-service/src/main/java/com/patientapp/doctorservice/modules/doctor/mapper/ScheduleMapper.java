package com.patientapp.doctorservice.modules.doctor.mapper;

import com.patientapp.doctorservice.modules.doctor.dto.ScheduleRequestDTO;
import com.patientapp.doctorservice.modules.doctor.dto.ScheduleResponseDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {
    public Schedule toEntity(ScheduleRequestDTO dto, Doctor doctor) {
        return Schedule.builder()
                .doctor(doctor)
                .dayOfWeek(dto.dayOfWeek())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .build();
    }

    public ScheduleResponseDTO toResponseDTO(Schedule entity) {
        return ScheduleResponseDTO.builder()
                .id(entity.getId())
                .doctorId(entity.getDoctor().getId())
                .dayOfWeek(entity.getDayOfWeek())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .zoneId(entity.getDoctor().getZoneId())
                .build();
    }

    public void updateEntity(Schedule entity, ScheduleRequestDTO dto) {
        entity.setDayOfWeek(dto.dayOfWeek());
        entity.setStartTime(dto.startTime());
        entity.setEndTime(dto.endTime());
    }
}

