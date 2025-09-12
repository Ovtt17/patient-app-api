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
import com.patientapp.doctorservice.modules.doctor.service.interfaces.ScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final DoctorService doctorService;
    private final ScheduleMapper scheduleMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ScheduleResponseDTO create(ScheduleRequestDTO request) {
        validateTimes(request.startTime(), request.endTime());

        boolean conflict = scheduleRepository.existsConflict(
                request.doctorId(),
                request.dayOfWeek(),
                request.startTime(),
                request.endTime(),
                null // null porque es creación, no hay id aún
        );

        if (conflict) {
            throw new ScheduleConflictException("El horario se solapa con otro existente del doctor");
        }

        Doctor doctor = doctorService.getEntityByIdOrThrow(request.doctorId());
        Schedule schedule = scheduleMapper.toEntity(request, doctor);
        return scheduleMapper.toResponseDTO(scheduleRepository.save(schedule));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleResponseDTO getById(Integer id) {
        Schedule schedule = getEntityByIdOrThrow(id);
        return scheduleMapper.toResponseDTO(schedule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schedule getEntityByIdOrThrow(Integer id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ScheduleNotFoundException("Horario no encontrado"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScheduleResponseDTO> getByDoctorId(UUID doctorId) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(doctorId);
        List<Schedule> schedules = scheduleRepository.findByDoctor(doctor);
        return schedules.stream().map(scheduleMapper::toResponseDTO).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScheduleResponseDTO> getByDoctorIdAndDay(UUID doctorId, DayOfWeek dayOfWeek) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(doctorId);
        List<Schedule> schedules = scheduleRepository.findByDoctorAndDayOfWeek(doctor, dayOfWeek);

        return schedules.stream().map(scheduleMapper::toResponseDTO).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ScheduleResponseDTO update(Integer id, ScheduleRequestDTO request) {
        Schedule schedule = getEntityByIdOrThrow(id);
        validateTimes(request.startTime(), request.endTime());

        boolean conflict = scheduleRepository.existsConflict(
                schedule.getDoctor().getId(),
                request.dayOfWeek(),
                request.startTime(),
                request.endTime(),
                schedule.getId() // Excluir el horario que estamos actualizando
        );

        if (conflict) {
            throw new ScheduleConflictException("El horario se solapa con otro existente del doctor");
        }

        scheduleMapper.updateEntity(schedule, request);
        return scheduleMapper.toResponseDTO(scheduleRepository.save(schedule));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        Schedule schedule = getEntityByIdOrThrow(id);
        scheduleRepository.delete(schedule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllByDoctorId(UUID doctorId) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(doctorId);
        List<Schedule> schedules = scheduleRepository.findByDoctor(doctor);
        scheduleRepository.deleteAll(schedules);
    }

    /**
     * Validates that the start time is before the end time.
     *
     * @param startTime The start time
     * @param endTime   The end time
     * @throws IllegalArgumentException if the start time is not before the end time
     */
    private void validateTimes(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin");
        }
    }
}

