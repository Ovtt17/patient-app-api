package com.patientapp.medicalrecordservice.modules.medicalrecord.entity;

import com.patientapp.medicalrecordservice.common.model.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medical_records")
public class MedicalRecord extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID patientId;
    private Long appointmentId;

    private Double weight;
    private Double height;
    private String bloodType;
    private String allergies;
    private String chronicDiseases;
    private String medications;
    private String diagnostic;
}
