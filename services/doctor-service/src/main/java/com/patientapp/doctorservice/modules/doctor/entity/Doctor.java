package com.patientapp.doctorservice.modules.doctor.entity;

import com.patientapp.doctorservice.common.model.BaseAuditingEntity;
import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String medicalLicense;

    @Column
    private String officeNumber;

    @Column(nullable = false)
    private boolean active;

    // Auth Service User ID
    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    private String zoneId;

    @ManyToMany
    @JoinTable(
            name = "doctor_specialties",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private Set<Specialty> specialties;
}
