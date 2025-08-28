package com.patientapp.doctorservice.doctor.entity;

import com.patientapp.doctorservice.specialty.entity.Specialty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String medicalLicense;

    @Column(length = 15)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String officeNumber;

    @Column(nullable = false)
    private boolean active;

    // Auth Service User ID
    @Column(nullable = false, unique = true)
    private UUID userId;

    @ManyToMany
    @JoinTable(
            name = "doctor_specialties",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private Set<Specialty> specialties;
}
