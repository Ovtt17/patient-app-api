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
    private Instant appointmentStart;              // Hora de inicio de la cita enviada por el frontend

    @Column(nullable = false)
    private Integer plannedDurationMinutes;        // Duración límite/estimada de la cita según el doctor (ej. 30 min)

    private Instant appointmentEnd;                // Hora exacta en que finalizó la cita (se asigna al finalizar)

    private Integer actualDurationMinutes;         // Duración real de la cita en minutos (calculada al finalizar)

    @Column(nullable = false)
    private String reason;                          // Motivo obligatorio de la cita

    private String notes;                           // Notas internas u observaciones

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;               // Estado: SCHEDULED, CANCELLED, COMPLETED, etc.

    private UUID cancelledBy;                        // Usuario que canceló la cita
    private Instant cancelledDate;                  // Fecha de cancelación
}
