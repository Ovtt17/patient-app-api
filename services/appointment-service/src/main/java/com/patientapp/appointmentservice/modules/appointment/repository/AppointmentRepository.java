package com.patientapp.appointmentservice.modules.appointment.repository;

import com.patientapp.appointmentservice.modules.appointment.entity.Appointment;
import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
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
        SELECT a FROM Appointment a
        WHERE (:doctorId IS NULL OR a.doctorId = :doctorId)
          AND (:patientId IS NULL OR a.patientId = :patientId)
          AND (:status IS NULL OR a.status = :status)
          AND (:startDate IS NULL OR a.appointmentStart >= :startDate)
          AND (:endDate IS NULL OR a.appointmentStart <= :endDate)
        """)
    List<Appointment> findAllFiltered(UUID doctorId, UUID patientId,
                                      AppointmentStatus status,
                                      Instant startDate,
                                      Instant endDate);


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
}