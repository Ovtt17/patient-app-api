package com.patientapp.doctorservice.modules.doctor.service.impl;

import com.patientapp.doctorservice.modules.doctor.dto.DoctorAvailabilityResponseDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorAvailabilityResponseDTO.DayAvailabilityDTO;
import com.patientapp.doctorservice.modules.doctor.dto.DoctorAvailabilityResponseDTO.IntervalDTO;
import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.DoctorUnavailability;
import com.patientapp.doctorservice.modules.doctor.entity.Schedule;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorAvailabilityService;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorService;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.DoctorUnavailabilityService;
import com.patientapp.doctorservice.modules.doctor.service.interfaces.ScheduleService;
import com.patientapp.doctorservice.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {
    private final DoctorService doctorService;
    private final ScheduleService scheduleService;
    private final DoctorUnavailabilityService unavailabilityService;
    private final JwtUtils jwtUtils;

    /**
     * {@inheritDoc}
     */
    @Override
    public DoctorAvailabilityResponseDTO getByDoctorId(UUID doctorId) {
        Doctor doctor = doctorService.getEntityByIdOrThrow(doctorId);
        ZoneId zone = ZoneId.of(doctor.getZoneId());

        List<Schedule> schedules = scheduleService.getAllEntitiesByDoctorIdOrThrow(doctorId);
        List<DoctorUnavailability> unavailabilities = unavailabilityService.getAllEntitiesByDoctorId(doctorId);

        List<DayAvailabilityDTO> availability = buildWeeklyAvailability(schedules, unavailabilities, zone);

        return DoctorAvailabilityResponseDTO.builder()
                .doctorId(doctorId)
                .zoneId(doctor.getZoneId())
                .availability(availability)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoctorAvailabilityResponseDTO getMyAvailability() {
        UUID userId = jwtUtils.getUserIdOrThrow();
        Doctor doctor = doctorService.getEntityByUserIdOrThrow(userId);
        return getByDoctorId(doctor.getId());
    }

    /**
     * Builds the weekly availability for the doctor, subtracting absences from available intervals.
     *
     * @param schedules List of doctor's schedules
     * @param unavailabilities List of doctor's absences
     * @param zone Doctor's time zone
     * @return List of DayAvailabilityDTO for each day
     */
    private List<DayAvailabilityDTO> buildWeeklyAvailability(
            List<Schedule> schedules,
            List<DoctorUnavailability> unavailabilities,
            ZoneId zone
    ) {
        LocalDate today = LocalDate.now(zone);
        return IntStream.range(0, 7)
                .mapToObj(today::plusDays)
                .map(date -> buildDayAvailability(date, schedules, unavailabilities, zone))
                .collect(Collectors.toList());
    }

    /**
     * Builds the availability intervals for a specific day, subtracting absences from available intervals.
     *
     * @param date The date to build availability for
     * @param schedules List of doctor's schedules
     * @param unavailabilities List of doctor's absences
     * @param zone Doctor's time zone
     * @return DayAvailabilityDTO for the given day
     */
    private DayAvailabilityDTO buildDayAvailability(
            LocalDate date,
            List<Schedule> schedules,
            List<DoctorUnavailability> unavailabilities,
            ZoneId zone
    ) {
        // Build schedule intervals for the day
        List<IntervalDTO> scheduleIntervals = schedules.stream()
                .filter(s -> s.getDayOfWeek() == date.getDayOfWeek())
                .map(s -> new IntervalDTO(
                            parseToInstant(s.getStartTime(), date, zone),
                            parseToInstant(s.getEndTime(), date, zone)
                        )
                )
                .toList();

        // Build unavailable intervals for the day
        // An absence overlaps with the day if it starts before the end of the day and ends after the start of the day
        List<IntervalDTO> unavailableIntervals = unavailabilities.stream()
                .filter(u ->
                        !u.getEndTime().isBefore(date.atStartOfDay(zone).toInstant())
                        && !u.getStartTime().isAfter(date.atTime(LocalTime.MAX).atZone(zone).toInstant())
                )
                .map(u -> new IntervalDTO(
                            u.getStartTime(),
                            u.getEndTime()
                        )
                )
                .toList();

        // Subtract unavailable intervals from schedule intervals
        List<IntervalDTO> availableIntervals = subtractUnavailableIntervals(scheduleIntervals, unavailableIntervals);

        // Build and return the day's availability DTO
        return DayAvailabilityDTO.builder()
                .date(date)
                .dayOfWeek(date.getDayOfWeek().name())
                .intervals(availableIntervals)
                .unavailable(unavailableIntervals)
                .build();
    }

    /**
     * Subtracts unavailable intervals (absences) from available intervals (schedule).
     * Returns the resulting available intervals after removing overlaps.
     *
     * @param intervals List of available intervals
     * @param unavailable List of unavailable intervals
     * @return List of IntervalDTO representing available intervals after subtraction
     */
    private List<IntervalDTO> subtractUnavailableIntervals(List<IntervalDTO> intervals, List<IntervalDTO> unavailable) {
        List<IntervalDTO> result = new ArrayList<>();
        for (IntervalDTO interval : intervals) {
            Instant start = interval.start();
            Instant end = interval.end();

            // Find all unavailable intervals that overlap with this available interval
            List<IntervalDTO> overlaps = unavailable.stream()
                    .filter(u -> u.start().isBefore(end) && u.end().isAfter(start))
                    .toList();

            // If no overlaps, keep the interval as is
            if (overlaps.isEmpty()) {
                result.add(interval);
                continue;
            }

            Instant currentStart = start;
            for (IntervalDTO u : overlaps) {
                // Add any time before the unavailable period
                if (u.start().isAfter(currentStart)) {
                    result.add(new IntervalDTO(currentStart, u.start()));
                }
                // Move the start pointer past the unavailable period
                if (u.end().isAfter(currentStart)) {
                    currentStart = u.end();
                }
            }

            // Add any remaining time after the last unavailable period
            if (currentStart.isBefore(end)) {
                result.add(new IntervalDTO(currentStart, end));
            }
        }
        return result;
    }

    /**
     * Parses a LocalTime and LocalDate to an Instant in the given ZoneId.
     *
     * @param time LocalTime to parse
     * @param date LocalDate to parse
     * @param zone ZoneId for the conversion
     * @return Instant representing the date and time in the given zone
     */
    private Instant parseToInstant(LocalTime time, LocalDate date, ZoneId zone) {
        return time.atDate(date).atZone(zone).toInstant();
    }
}