package com.patientapp.appointmentservice.modules.appointment.entity;

import com.patientapp.appointmentservice.common.model.BaseAuditingEntity;
import com.patientapp.appointmentservice.modules.appointment.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID doctorId;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    Instant appointmentDate;

    private Integer estimatedDurationMinutes;
    private Instant endTime;

    private String reason;

    private String notes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private UUID cancelledBy; // User ID who cancelled the appointment
    private Instant cancelledDate;
}
