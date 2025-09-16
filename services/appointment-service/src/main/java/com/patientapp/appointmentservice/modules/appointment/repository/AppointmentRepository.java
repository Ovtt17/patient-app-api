package com.patientapp.appointmentservice.modules.appointment.repository;

import com.patientapp.appointmentservice.modules.appointment.entity.Appointment;
import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndAppointmentDateAfter(UUID doctorId, Instant fromDate);

    List<Appointment> findByPatientIdAndAppointmentDateAfter(UUID patientId, Instant fromDate);
    @Query("""
        SELECT a FROM Appointment a
        WHERE (:doctorId IS NULL OR a.doctorId = :doctorId)
          AND (:patientId IS NULL OR a.patientId = :patientId)
          AND (:status IS NULL OR a.status = :status)
          AND (:startDate IS NULL OR a.appointmentDate >= :startDate)
          AND (:endDate IS NULL OR a.appointmentDate <= :endDate)
        """)
    List<Appointment> findFiltered(UUID doctorId, UUID patientId,
                                   AppointmentStatus status,
                                   Instant startDate,
                                   Instant endDate);
}