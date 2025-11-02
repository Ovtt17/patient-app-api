package com.patientapp.appointmentservice.modules.appointment.repository;

import com.patientapp.appointmentservice.modules.appointment.entity.Appointment;
import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findAllByDoctorId(UUID doctorId);

    List<Appointment> findAllByPatientId(UUID patientId);

    @Query("""
    SELECT FUNCTION('DATE', a.appointmentStart) AS day,
           COUNT(a) AS total
    FROM Appointment a
    WHERE a.doctorId = :doctorId
      AND a.appointmentStart BETWEEN :start AND :end
    GROUP BY FUNCTION('DATE', a.appointmentStart)
""")
    List<Object[]> countAppointmentsByDoctorAndDateRaw(
            @Param("doctorId") UUID doctorId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    @Query("""
        SELECT a.appointmentStart
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.appointmentStart >= :dayStart
          AND a.appointmentStart <= :dayEnd
    """)
    List<Instant> findAppointmentsByDoctorAndDay(
            @Param("doctorId") UUID doctorId,
            @Param("dayStart") Instant dayStart,
            @Param("dayEnd") Instant dayEnd
    );

    long countByAppointmentStartBetween(Instant start, Instant end);

    long countByAppointmentStartBetweenAndStatus(Instant start, Instant end, AppointmentStatus appointmentStatus);

    @Query("""
        SELECT a FROM Appointment a
        WHERE a.appointmentStart BETWEEN :start AND :end
        ORDER BY a.appointmentStart DESC
    """)
    List<Appointment> findRecentAppointments(@Param("start") Instant start, @Param("end") Instant end);

    @Query("""
        SELECT a.doctorId AS doctorId, COUNT(a) AS count
        FROM Appointment a
        WHERE a.appointmentStart BETWEEN :start AND :end
        GROUP BY a.doctorId
        ORDER BY COUNT(a) DESC
    """)
    List<DoctorAppointmentCount> findTopDoctors(@Param("start") Instant start, @Param("end") Instant end, Pageable pageable);

    @Query("""
        SELECT COUNT(a)
        FROM Appointment a
        WHERE a.appointmentStart BETWEEN :startDate AND :endDate
    """)
    Integer countAppointmentsByDateRange(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );

    @Query("""
        SELECT COUNT(DISTINCT a.patientId)
        FROM Appointment a
        WHERE a.doctorId = :doctorId
            AND a.status IN ('PENDIENTE', 'CONFIRMADA', 'COMPLETADA')
        """)
    Long countDistinctPatientsByDoctorId(@Param("doctorId") UUID doctorId);

    @Query("""
        SELECT COUNT(a)
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.appointmentStart BETWEEN :start AND :end
          AND a.status IN ('PENDIENTE', 'CONFIRMADA', 'COMPLETADA')
    """)
    Integer countAppointmentsByDoctorAndDateRange(
            @Param("doctorId") UUID doctorId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    @Query("""
        SELECT COUNT(a)
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.appointmentStart BETWEEN :start AND :end
          AND a.status = 'COMPLETADA'
    """)
    Long countCompletedAppointmentsByDoctorAndDateRange(
            @Param("doctorId") UUID doctorId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    @Query("""
        SELECT COUNT(a)
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.appointmentStart BETWEEN :start AND :end
          AND a.status = 'COMPLETADA'
    """)
    Long countCancelledAppointmentsByDoctorAndDateRange(
            @Param("doctorId") UUID doctorId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.appointmentStart BETWEEN :start AND :end
        ORDER BY a.appointmentStart DESC
    """)
    List<Appointment> findRecentAppointmentsByDoctor(
            @Param("doctorId") UUID doctorId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}