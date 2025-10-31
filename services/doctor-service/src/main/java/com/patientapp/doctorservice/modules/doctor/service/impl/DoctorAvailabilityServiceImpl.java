package com.patientapp.doctorservice.modules.doctor.service.impl;

import com.patientapp.doctorservice.modules.appointment.client.AppointmentClient;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorDayAvailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorMonthAvailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.dto.IntervalDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.DoctorUnavailability;
import com.patientapp.doctorservice.modules.doctor.entity.Schedule;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorAvailabilityService;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorService;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorUnavailabilityService;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private final DoctorService doctorService;
    private final ScheduleService scheduleService;
    private final DoctorUnavailabilityService unavailabilityService;
    private final AppointmentClient appointmentClient;

    /**
     * Contexto que encapsula toda la información necesaria para calcular
     * la disponibilidad de un doctor en un día específico.
     */
    private record DoctorDayContext(
            LocalDate date,
            ZoneId zone,
            Doctor doctor,
            List<Schedule> schedules,
            List<DoctorUnavailability> unavailabilities,
            Map<LocalDate, Long> appointmentCounts
    ) {}

    @Override
    public DoctorMonthAvailabilityResponseDTO getByDoctorIdAndMonth(UUID doctorId, Month month) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(doctorId);
        ZoneId zone = ZoneId.of(doctor.getZoneId());
        YearMonth yearMonth = YearMonth.of(Year.now(zone).getValue(), month);

        List<Schedule> schedules = scheduleService.getAllEntitiesByDoctorIdOrThrow(doctorId);
        List<DoctorUnavailability> unavailabilities = unavailabilityService.getAllEntitiesByDoctorId(doctorId);

        Map<LocalDate, Long> appointmentCounts = appointmentClient.getAppointmentCountByDoctorAndMonth(
                doctorId,
                yearMonth.getYear(),
                month.getValue()
        );

        List<DoctorMonthAvailabilityResponseDTO.DayAvailability> availabilities =
                calculateMonthlyAvailability(yearMonth, doctor, schedules, unavailabilities, appointmentCounts, zone);

        return DoctorMonthAvailabilityResponseDTO.builder()
                .doctorId(doctorId)
                .year(yearMonth.getYear())
                .month(month.getValue())
                .availability(availabilities)
                .build();
    }

    @Override
    public DoctorDayAvailabilityResponseDTO getByDoctorIdAndDay(UUID doctorId, LocalDate date) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(doctorId);
        ZoneId zone = ZoneId.of(doctor.getZoneId());

        List<Schedule> schedules = scheduleService.getAllEntitiesByDoctorIdOrThrow(doctorId)
                .stream()
                .filter(s -> s.getDayOfWeek() == date.getDayOfWeek())
                .toList();

        List<DoctorUnavailability> unavailabilities = unavailabilityService.getAllEntitiesByDoctorId(doctorId)
                .stream()
                .filter(u ->
                        !u.getEndTime().isBefore(date.atStartOfDay(zone).toInstant()) &&
                                !u.getStartTime().isAfter(date.atTime(LocalTime.MAX).atZone(zone).toInstant())
                ).toList();

        List<Instant> bookedInstants = appointmentClient.getAppointmentsByDoctorAndDay(doctorId, date);

        List<IntervalDTO> scheduleIntervals = schedules.stream()
                .map(s -> new IntervalDTO(
                        toInstant(s.getStartTime(), date, zone),
                        toInstant(s.getEndTime(), date, zone)
                ))
                .toList();

        List<IntervalDTO> mergedUnavailable = mergeIntervals(unavailabilities, bookedInstants, doctor.getAppointmentDuration());

        List<IntervalDTO> availableIntervals = subtractUnavailableIntervals(
                scheduleIntervals,
                mergedUnavailable
        );

        return new DoctorDayAvailabilityResponseDTO(
                doctorId,
                date,
                doctor.getAppointmentDuration(),
                availableIntervals
        );
    }


    private Instant toInstant(LocalTime time, LocalDate date, ZoneId zone) {
        return time.atDate(date).atZone(zone).toInstant();
    }

    private List<IntervalDTO> buildScheduleIntervals(List<Schedule> schedules, DoctorDayContext ctx) {
        return schedules.stream()
                .map(s -> new IntervalDTO(
                        toInstant(s.getStartTime(), ctx.date(), ctx.zone()),
                        toInstant(s.getEndTime(), ctx.date(), ctx.zone())
                ))
                .toList();
    }

    private List<IntervalDTO> mergeIntervals(
            List<DoctorUnavailability> unavailabilities,
            List<Instant> appointments,
            int appointmentDurationMinutes
    ) {
        List<IntervalDTO> intervals = new ArrayList<>();

        // Convertir indisponibilidades a IntervalDTO
        intervals.addAll(
                unavailabilities.stream()
                        .map(u -> new IntervalDTO(u.getStartTime(), u.getEndTime()))
                        .toList()
        );

        // Convertir citas ocupadas (Instant) a IntervalDTO según la duración de cita
        intervals.addAll(
                appointments.stream()
                        .map(start -> new IntervalDTO(
                                start,
                                start.plus(Duration.ofMinutes(appointmentDurationMinutes))
                        ))
                        .toList()
        );

        return intervals;
    }

    private List<IntervalDTO> buildUnavailableIntervals(DoctorDayContext ctx) {
        Instant dayStart = ctx.date().atStartOfDay(ctx.zone()).toInstant();
        Instant dayEnd = ctx.date().atTime(LocalTime.MAX).atZone(ctx.zone()).toInstant();

        return ctx.unavailabilities().stream()
                .filter(u -> !u.getEndTime().isBefore(dayStart) && !u.getStartTime().isAfter(dayEnd))
                .map(u -> new IntervalDTO(u.getStartTime(), u.getEndTime()))
                .toList();
    }

    private List<IntervalDTO> subtractUnavailableIntervals(List<IntervalDTO> intervals, List<IntervalDTO> unavailable) {
        List<IntervalDTO> result = new ArrayList<>();

        for (IntervalDTO interval : intervals) {
            Instant start = interval.start();
            Instant end = interval.end();

            List<IntervalDTO> overlaps = unavailable.stream()
                    .filter(u -> u.start().isBefore(end) && u.end().isAfter(start))
                    .sorted(Comparator.comparing(IntervalDTO::start))
                    .toList();

            if (overlaps.isEmpty()) {
                result.add(interval);
                continue;
            }

            Instant currentStart = start;
            for (IntervalDTO u : overlaps) {
                if (u.start().isAfter(currentStart)) result.add(new IntervalDTO(currentStart, u.start()));
                if (u.end().isAfter(currentStart)) currentStart = u.end();
            }

            if (currentStart.isBefore(end)) result.add(new IntervalDTO(currentStart, end));
        }

        return result;
    }

    private long calculateTotalCapacity(List<IntervalDTO> intervals, int appointmentMinutes) {
        return intervals.stream()
                .mapToLong(i -> Duration.between(i.start(), i.end()).toMinutes() / appointmentMinutes)
                .sum();
    }

    /**
     * Determina si un día está completamente ocupado o bloqueado.
     */
    private boolean isDayFullyBooked(DoctorDayContext ctx) {
        List<Schedule> dailySchedules = ctx.schedules().stream()
                .filter(s -> s.getDayOfWeek() == ctx.date().getDayOfWeek())
                .toList();

        if (dailySchedules.isEmpty()) return true; // no trabaja

        List<IntervalDTO> scheduleIntervals = buildScheduleIntervals(dailySchedules, ctx);
        List<IntervalDTO> unavailableIntervals = buildUnavailableIntervals(ctx);
        List<IntervalDTO> availableIntervals = subtractUnavailableIntervals(scheduleIntervals, unavailableIntervals);

        if (availableIntervals.isEmpty()) return true; // todas las horas cubiertas por ausencias

        long capacity = calculateTotalCapacity(availableIntervals, ctx.doctor().getAppointmentDuration());
        long booked = ctx.appointmentCounts().getOrDefault(ctx.date(), 0L);

        return booked >= capacity;
    }

    private List<DoctorMonthAvailabilityResponseDTO.DayAvailability> calculateMonthlyAvailability(
            YearMonth yearMonth,
            Doctor doctor,
            List<Schedule> schedules,
            List<DoctorUnavailability> unavailabilities,
            Map<LocalDate, Long> appointmentCounts,
            ZoneId zone
    ) {
        List<DoctorMonthAvailabilityResponseDTO.DayAvailability> result = new ArrayList<>();

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            DoctorDayContext ctx = new DoctorDayContext(date, zone, doctor, schedules, unavailabilities, appointmentCounts);
            boolean fullyBooked = isDayFullyBooked(ctx);
            result.add(new DoctorMonthAvailabilityResponseDTO.DayAvailability(date, fullyBooked));
        }

        return result;
    }
}

