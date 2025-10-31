package com.patientapp.appointmentservice.modules.appointment.service.impl;

import com.patientapp.appointmentservice.common.utils.AuthUtil;
import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentDayCountDTO;
import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentFilterDTO;
import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentRequestDTO;
import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentResponseDTO;
import com.patientapp.appointmentservice.modules.appointment.entity.Appointment;
import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
import com.patientapp.appointmentservice.modules.appointment.mapper.AppointmentMapper;
import com.patientapp.appointmentservice.modules.appointment.repository.AppointmentRepository;
import com.patientapp.appointmentservice.modules.appointment.repository.AppointmentSpecifications;
import com.patientapp.appointmentservice.modules.appointment.service.interfaces.AppointmentService;
import com.patientapp.appointmentservice.modules.doctor.client.DoctorClient;
import com.patientapp.appointmentservice.modules.doctor.dto.DoctorResponse;
import com.patientapp.appointmentservice.modules.notification.AppointmentCreatedRequest;
import com.patientapp.appointmentservice.modules.notification.NotificationProducer;
import com.patientapp.appointmentservice.modules.patient.client.PatientClient;
import com.patientapp.appointmentservice.modules.patient.dto.PatientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
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
        return mapper.toResponse(appointmentSaved, doctor, patient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AppointmentResponseDTO getById(Long appointmentId) {
        Appointment appointment = getEntityByIdOrThrow(appointmentId);

        DoctorResponse doctor = doctorClient.getById(appointment.getDoctorId());
        PatientResponse patient = patientClient.getById(appointment.getPatientId());

        return mapper.toResponse(appointment, doctor, patient);
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
        List<Appointment> appointments = repository.findAllByDoctorId(doctorId);

        if (appointments.isEmpty()) return List.of();

        DoctorResponse doctor = doctorClient.getById(doctorId);

        List<UUID> patientIds = appointments.stream()
                .map(Appointment::getPatientId)
                .distinct()
                .toList();

        List<PatientResponse> patients = patientClient.getByIds(patientIds);

        Map<UUID, PatientResponse> patientMap = patients.stream()
                .collect(Collectors.toMap(PatientResponse::id, p -> p));

        return appointments.stream()
                .map(appointment -> {
                    PatientResponse patient = patientMap.get(appointment.getPatientId());
                    return mapper.toResponse(appointment, doctor, patient);
                })
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
        List<Appointment> appointments = repository.findAllByPatientId(patientId);

        if (appointments.isEmpty()) return List.of();

        PatientResponse patient = patientClient.getById(patientId);

        return appointments.stream()
                .map(appointment -> {
                    DoctorResponse doctor = doctorClient.getById(appointment.getDoctorId());
                    return mapper.toResponse(appointment, doctor, patient);
                })
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AppointmentResponseDTO> getAllFiltered(AppointmentFilterDTO filter) {
        Specification<Appointment> spec = AppointmentSpecifications.filterAppointments(filter);

        var appointments = repository.findAll(spec);

        if (appointments.isEmpty()) return List.of();

        // Caso mixto o sin filtros: resolver con batch doble
        List<UUID> doctorIds = appointments.stream()
                .map(Appointment::getDoctorId)
                .distinct()
                .toList();

        List<UUID> patientIds = appointments.stream()
                .map(Appointment::getPatientId)
                .distinct()
                .toList();

        Map<UUID, DoctorResponse> doctorMap = doctorClient.getByIds(doctorIds).stream()
                .collect(Collectors.toMap(DoctorResponse::id, d -> d));

        Map<UUID, PatientResponse> patientMap = patientClient.getByIds(patientIds).stream()
                .collect(Collectors.toMap(PatientResponse::id, p -> p));

        return appointments.stream()
                .map(appointment -> {
                    DoctorResponse doctor = doctorMap.get(appointment.getDoctorId());
                    PatientResponse patient = patientMap.get(appointment.getPatientId());
                    return mapper.toResponse(appointment, doctor, patient);
                })
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Long updateStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = getEntityByIdOrThrow(appointmentId);
        appointment.setStatus(status);
        return repository.save(appointment).getId();
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
