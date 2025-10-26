package com.patientapp.appointmentservice.modules.appointment.service.impl;

import com.patientapp.appointmentservice.common.utils.AuthUtil;
import com.patientapp.appointmentservice.modules.appointment.dto.*;
import com.patientapp.appointmentservice.modules.appointment.entity.Appointment;
import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
import com.patientapp.appointmentservice.modules.appointment.mapper.AppointmentMapper;
import com.patientapp.appointmentservice.modules.appointment.repository.AppointmentRepository;
import com.patientapp.appointmentservice.modules.appointment.service.interfaces.AppointmentService;
import com.patientapp.appointmentservice.modules.doctor.client.DoctorClient;
import com.patientapp.appointmentservice.modules.doctor.dto.DoctorResponse;
import com.patientapp.appointmentservice.modules.notification.AppointmentCreatedRequest;
import com.patientapp.appointmentservice.modules.notification.NotificationProducer;
import com.patientapp.appointmentservice.modules.patient.client.PatientClient;
import com.patientapp.appointmentservice.modules.patient.dto.PatientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;
    private final NotificationProducer notificationProducer;

    private static final ZoneId ZONE = ZoneId.of("America/Managua");


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AppointmentResponseDTO create(AppointmentRequestDTO request) {
        PatientResponse patient = patientClient.getByUserId(request.userId());
        DoctorResponse doctor = doctorClient.getById(request.doctorId());

        AppointmentRequestDTO requestWithIds = AppointmentRequestDTO.builder()
                .doctorId(doctor.id())
                .patientId(patient.id())
                .appointmentStart(request.appointmentStart())
                .reason(request.reason())
                .userId(request.userId())
                .build();

        Appointment appointment = mapper.toEntity(requestWithIds);
        appointment.setStatus(AppointmentStatus.PENDIENTE);

        int durationMinutes = doctor.appointmentDuration();
        appointment.setPlannedDurationMinutes(durationMinutes);

        Appointment appointmentSaved = repository.save(appointment);

        var appointCreatedRequest = AppointmentCreatedRequest.builder()
                .appointmentId(appointmentSaved.getId().toString())
                .patientName(patient.firstName())
                .patientEmail(patient.email())
                .doctorName(doctor.firstName())
                .doctorEmail(doctor.email())
                .doctorZoneId(doctor.zoneId())
                .appointmentStart(appointmentSaved.getAppointmentStart())
                .build();

        notificationProducer.sendAppointmentCreatedEvent(appointCreatedRequest);
        return mapper.toResponse(appointmentSaved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AppointmentResponseDTO getById(Long appointmentId) {
        Appointment appointment = getEntityByIdOrThrow(appointmentId);
        return mapper.toResponse(appointment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Appointment getEntityByIdOrThrow(Long appointmentId) {
        return repository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AppointmentResponseDTO> getAllByDoctor(
            UUID doctorId,
            Instant fromDate
    ) {
        Instant from = fromDate != null ? fromDate : Instant.now().plus(7, ChronoUnit.DAYS);
        List<Appointment> appointments = repository.findByDoctorIdAndAppointmentStartAfter(
                doctorId,
                from
        );

        if (appointments.isEmpty()) return List.of();

        return appointments.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AppointmentResponseDTO> getAllByPatient(
            UUID patientId,
            Instant fromDate
    ) {
        Instant from = fromDate != null ? fromDate : Instant.now().plus(7, ChronoUnit.DAYS);
        List<Appointment> appointments = repository.findByPatientIdAndAppointmentStartAfter(
                patientId,
                from
        );

        if (appointments.isEmpty()) return List.of();

        return appointments.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AppointmentResponseDTO> getAllFiltered(AppointmentFilterDTO filter) {
        return repository.findFiltered(
                        filter.doctorId(),
                        filter.patientId(),
                        filter.status(),
                        filter.startDate(),
                        filter.endDate()
                ).stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = getEntityByIdOrThrow(appointmentId);
        appointment.setStatus(status);
        repository.save(appointment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void cancel(Long appointmentId) {
        Appointment appointment = getEntityByIdOrThrow(appointmentId);
        String userId = AuthUtil.getUserIdFromJwt();
        if (userId != null) {
            appointment.setCancelledBy(UUID.fromString(userId));
        }

        appointment.setStatus(AppointmentStatus.CANCELADA);
        appointment.setCancelledDate(Instant.now());
        repository.save(appointment);
    }

    @Override
    public Map<LocalDate, Long> getAppointmentCountByDoctorAndMonth(UUID doctorId, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        Instant start = ym.atDay(1).atStartOfDay(ZONE).toInstant();
        Instant end = ym.atEndOfMonth().atTime(23, 59, 59).atZone(ZONE).toInstant();

        List<Object[]> rawResults = repository.countAppointmentsByDoctorAndDateRaw(doctorId, start, end);

        List<AppointmentDayCountDTO> results = rawResults.stream()
                .map(o -> new AppointmentDayCountDTO(
                        ((java.sql.Date) o[0]).toLocalDate(),
                        ((Number) o[1]).longValue()
                ))
                .toList();
        return results.stream()
                .collect(Collectors.toMap(AppointmentDayCountDTO::getDate, AppointmentDayCountDTO::getCount));
    }

    @Override
    public List<Instant> getAppointmentsByDoctorAndDay(UUID doctorId, LocalDate date) {
        Instant dayStart = date.atStartOfDay(ZONE).toInstant();
        Instant dayEnd = date.atTime(LocalTime.MAX).atZone(ZONE).toInstant();

        return repository.findAppointmentsByDoctorAndDay(doctorId, dayStart, dayEnd);
    }

}
