package com.patientapp.doctorservice.modules.doctor.repository;

import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByDoctor(Doctor doctor);
    List<Schedule> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);

    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
        FROM Schedule s
        WHERE s.doctor.id = :doctorId
          AND s.dayOfWeek = :dayOfWeek
          AND (:scheduleId IS NULL OR s.id <> :scheduleId)
          AND s.startTime < :endTime
          AND s.endTime > :startTime
    """)
    boolean existsConflict(
            @Param("doctorId") UUID doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("scheduleId") Integer scheduleId
    );
}
